# Integration Guide for Java Application

This guide shows how to integrate the File API with your Java-based WhereTheCar application.

## Overview

The File API handles all file storage and retrieval operations, keeping your PostgreSQL database clean. Files are stored in the filesystem and referenced by UUID in your database.

## Database Schema

Your database should only store:
- UUID of the file
- Original filename (for user-friendly downloads)
- File type/category (optional)

Example tables (matching your ER diagram):

```sql
-- Already in your schema
CREATE TABLE picture (
    uuid UUID PRIMARY KEY,
    vehicle_uuid UUID REFERENCES vehicle(uuid),
    name VARCHAR(255),  -- Original filename for display
    file_path VARCHAR(500)  -- Can store the API URL or just the UUID
);

CREATE TABLE maintenance_document (
    uuid UUID PRIMARY KEY,
    operation_uuid UUID REFERENCES maintenance_operation(uuid),
    name VARCHAR(255),  -- Original filename
    file_path VARCHAR(500)  -- API URL or UUID
);
```

## Java Integration Examples

### 1. Configuration

Add the File API URL to your application configuration:

```java
// application.properties or similar
file.api.url=http://localhost:8080
# Or for production
# file.api.url=http://files.yourserver.com
```

### 2. File Service Class

Create a service to handle file operations:

```java
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileApiService {
    private final String fileApiUrl;
    private final HttpClient httpClient;
    private final Path pdfStoragePath;
    private final Path pictureStoragePath;
    
    public FileApiService(String fileApiUrl, Path pdfStoragePath, Path pictureStoragePath) {
        this.fileApiUrl = fileApiUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.pdfStoragePath = pdfStoragePath;
        this.pictureStoragePath = pictureStoragePath;
    }
    
    // Convenience constructor with default paths
    public FileApiService(String fileApiUrl) {
        this(fileApiUrl, 
             Path.of("./storage/pdf"), 
             Path.of("./storage/pictures"));
    }
    
    /**
     * Upload a picture file
     * 
     * NOTE: Since the File API doesn't provide upload endpoints (it only serves files),
     * you need to write directly to the storage directory that the File API reads from.
     * 
     * This assumes your Java app has access to the same filesystem as the File API
     * (same server, shared volume, or network mount).
     * 
     * @param file The file to upload
     * @param originalFilename Original filename for reference
     * @param storagePath The base path where the File API stores pictures
     * @return The UUID of the uploaded file
     */
    public UUID uploadPicture(Path file, String originalFilename, Path storagePath) throws IOException {
        UUID fileUuid = UUID.randomUUID();
        String extension = getFileExtension(originalFilename);
        
        // Create destination path with UUID as filename
        Path destinationPath = storagePath.resolve(fileUuid + "." + extension);
        
        // Ensure directory exists
        Files.createDirectories(storagePath);
        
        // Copy file to storage directory
        Files.copy(file, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileUuid;
    }
    
    /**
     * Upload a PDF document
     * 
     * NOTE: Since the File API doesn't provide upload endpoints (it only serves files),
     * you need to write directly to the storage directory that the File API reads from.
     * 
     * @param file The PDF file to upload
     * @param originalFilename Original filename
     * @param storagePath The base path where the File API stores PDFs
     * @return The UUID of the uploaded file
     */
    public UUID uploadPdf(Path file, String originalFilename, Path storagePath) throws IOException {
        UUID fileUuid = UUID.randomUUID();
        
        // Create destination path with UUID as filename
        Path destinationPath = storagePath.resolve(fileUuid + ".pdf");
        
        // Ensure directory exists
        Files.createDirectories(storagePath);
        
        // Copy file to storage directory
        Files.copy(file, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileUuid;
    }
    
    /**
     * Get a picture URL
     * @param uuid The UUID of the picture
     * @return The full URL to retrieve the picture
     */
    public String getPictureUrl(UUID uuid) {
        return fileApiUrl + "/picture/" + uuid;
    }
    
    /**
     * Get a PDF URL
     * @param uuid The UUID of the PDF
     * @return The full URL to retrieve the PDF
     */
    public String getPdfUrl(UUID uuid) {
        return fileApiUrl + "/pdf/" + uuid;
    }
    
    /**
     * Download a picture
     * @param uuid The UUID of the picture
     * @return InputStream of the file content
     */
    public InputStream downloadPicture(UUID uuid) throws IOException, InterruptedException {
        return downloadFile("/picture/" + uuid);
    }
    
    /**
     * Download a PDF
     * @param uuid The UUID of the PDF
     * @return InputStream of the file content
     */
    public InputStream downloadPdf(UUID uuid) throws IOException, InterruptedException {
        return downloadFile("/pdf/" + uuid);
    }
    
    /**
     * Delete a picture (removes from filesystem)
     * @param uuid The UUID of the picture to delete
     */
    public void deletePicture(UUID uuid) throws IOException {
        // Try common image extensions
        String[] extensions = {"png", "jpg", "jpeg", "gif", "webp", "bmp"};
        for (String ext : extensions) {
            Path filePath = pictureStoragePath.resolve(uuid + "." + ext);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return;
            }
        }
    }
    
    /**
     * Delete a PDF (removes from filesystem)
     * @param uuid The UUID of the PDF to delete
     */
    public void deletePdf(UUID uuid) throws IOException {
        Path filePath = pdfStoragePath.resolve(uuid + ".pdf");
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
    
    /**
     * Check if File API is healthy
     */
    public boolean isHealthy() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileApiUrl + "/health"))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Private helper methods
    
    private InputStream downloadFile(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileApiUrl + path))
                .GET()
                .build();
        
        HttpResponse<InputStream> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofInputStream());
        
        if (response.statusCode() != 200) {
            throw new IOException("File not found or error retrieving file: HTTP " 
                    + response.statusCode());
        }
        
        return response.body();
    }
    
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }
}
```

### 3. Usage Examples

#### Uploading a Vehicle Picture

```java
// In your vehicle management code
Path pdfStorage = Path.of("/path/to/storage/pdf");  // Must match File API storage path
Path pictureStorage = Path.of("/path/to/storage/pictures");  // Must match File API storage path
FileApiService fileService = new FileApiService("http://localhost:8080", pdfStorage, pictureStorage);

// User uploads a picture
Path uploadedFile = Path.of("/tmp/user-uploaded-image.jpg");
String originalFilename = "my-car-photo.jpg";

// Upload and get UUID
UUID pictureUuid = fileService.uploadPicture(uploadedFile, originalFilename, pictureStorage);

// Save to database
Picture picture = new Picture();
picture.setUuid(pictureUuid);
picture.setVehicleUuid(vehicleUuid);
picture.setName(originalFilename);
picture.setFilePath(fileService.getPictureUrl(pictureUuid)); // or just store UUID

pictureRepository.save(picture);
```

#### Uploading a Maintenance Document

```java
// User uploads maintenance document (PDF)
Path uploadedPdf = Path.of("/tmp/maintenance-invoice.pdf");
String originalFilename = "oil-change-receipt.pdf";

// Upload and get UUID (using the same fileService instance)
UUID documentUuid = fileService.uploadPdf(uploadedPdf, originalFilename, pdfStorage);

// Save to database
MaintenanceDocument document = new MaintenanceDocument();
document.setUuid(documentUuid);
document.setOperationUuid(maintenanceOperationUuid);
document.setName(originalFilename);
document.setFilePath(fileService.getPdfUrl(documentUuid));

documentRepository.save(document);
```

#### Displaying Pictures in UI

```java
// Get vehicle pictures from database
List<Picture> pictures = pictureRepository.findByVehicleUuid(vehicleUuid);

// Generate URLs for display
for (Picture picture : pictures) {
    String imageUrl = fileService.getPictureUrl(picture.getUuid());
    // Use imageUrl in your UI (JavaFX ImageView, HTML img tag, etc.)
    // Example: imageView.setImage(new Image(imageUrl));
}
```

#### Downloading a Document

```java
// Download a maintenance document
UUID documentUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

try (InputStream fileStream = fileService.downloadPdf(documentUuid)) {
    // Save to user's chosen location
    Path downloadPath = Path.of(System.getProperty("user.home"), 
            "Downloads", "maintenance-doc.pdf");
    Files.copy(fileStream, downloadPath, StandardCopyOption.REPLACE_EXISTING);
    System.out.println("Downloaded to: " + downloadPath);
} catch (IOException | InterruptedException e) {
    System.err.println("Failed to download document: " + e.getMessage());
}
```

#### Deleting Files

```java
// When deleting a vehicle or maintenance operation, clean up associated files
List<Picture> pictures = pictureRepository.findByVehicleUuid(vehicleUuid);

for (Picture picture : pictures) {
    try {
        // Delete from filesystem
        fileService.deletePicture(picture.getUuid());
        // Delete from database
        pictureRepository.delete(picture);
    } catch (IOException e) {
        logger.error("Failed to delete picture: " + picture.getUuid(), e);
    }
}
```

### 4. JavaFX Integration (for Desktop UI)

If you're using JavaFX for your UI:

```java
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Display a vehicle picture
ImageView imageView = new ImageView();
UUID pictureUuid = vehicle.getPictures().get(0).getUuid();
String imageUrl = fileService.getPictureUrl(pictureUuid);

// Load image from URL
Image image = new Image(imageUrl, true); // true for background loading
imageView.setImage(image);

// Handle PDF viewing
UUID pdfUuid = maintenanceDocument.getUuid();
String pdfUrl = fileService.getPdfUrl(pdfUuid);

// Open PDF in default browser/viewer
if (Desktop.isDesktopSupported()) {
    Desktop.getDesktop().browse(URI.create(pdfUrl));
}
```

### 5. Error Handling

```java
public class FileServiceException extends RuntimeException {
    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

// In your service
public UUID uploadPictureWithErrorHandling(Path file, String originalFilename, Path storagePath) {
    try {
        return uploadPicture(file, originalFilename, storagePath);
    } catch (IOException e) {
        throw new FileServiceException("Failed to upload picture: " + originalFilename, e);
    }
}
```

## Best Practices

1. **Always store both UUID and original filename** in your database
   - UUID for API retrieval
   - Original filename for user-friendly display and downloads

2. **Clean up orphaned files** periodically
   - Run a scheduled job to find files without database references

3. **Validate file types** before upload
   ```java
   private void validateImageFile(Path file) throws IOException {
       String contentType = Files.probeContentType(file);
       if (!contentType.startsWith("image/")) {
           throw new IllegalArgumentException("File must be an image");
       }
   }
   ```

4. **Handle concurrent uploads** properly
   - Use database transactions when storing file references

5. **Consider file size limits**
   ```java
   private void validateFileSize(Path file, long maxSizeBytes) throws IOException {
       long size = Files.size(file);
       if (size > maxSizeBytes) {
           throw new IllegalArgumentException(
               "File too large: " + size + " bytes (max: " + maxSizeBytes + ")");
       }
   }
   ```

## Production Deployment

For production, consider:

1. **Use a reverse proxy** (nginx) in front of the File API for:
   - HTTPS termination
   - Caching of frequently accessed files
   - Rate limiting

2. **Backup strategy** for the storage volumes:
   ```bash
   # Backup storage directory
   tar -czf storage-backup-$(date +%Y%m%d).tar.gz /path/to/storage
   ```

3. **Monitor disk space** on the File API server

4. **Use environment-specific URLs**:
   - Development: `http://localhost:8080`
   - Production: `https://files.yourcompany.com`

## Troubleshooting

### File Not Found
- Verify the UUID exists in your database
- Check the file exists in the storage directory
- Verify the file extension matches what's expected

### Permission Denied
- Ensure the File API process has read/write access to storage directories
- Check filesystem permissions: `ls -la storage/`

### Out of Disk Space
- Implement file cleanup for deleted vehicles/operations
- Monitor disk usage: `df -h`
- Consider compression for old files
