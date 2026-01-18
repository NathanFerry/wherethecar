use actix_web::{error, web, HttpResponse, Result};
use log::{error, info, warn};
use mime_guess;
use std::path::PathBuf;
use uuid::Uuid;

/// Health check endpoint
pub async fn health_check() -> Result<HttpResponse> {
    Ok(HttpResponse::Ok().json(serde_json::json!({
        "status": "ok",
        "service": "file-api"
    })))
}

/// Get PDF file by UUID
pub async fn get_pdf(uuid: web::Path<Uuid>, pdf_path: web::Data<PathBuf>) -> Result<HttpResponse> {
    let uuid = uuid.into_inner();
    info!("Request for PDF: {}", uuid);

    // Try common PDF extensions
    let extensions = ["pdf"];

    for ext in extensions.iter() {
        let file_path = pdf_path.join(format!("{}.{}", uuid, ext));

        if file_path.exists() {
            info!("Found PDF file: {}", file_path.display());

            match std::fs::read(&file_path) {
                Ok(contents) => {
                    return Ok(HttpResponse::Ok()
                        .content_type("application/pdf")
                        .body(contents));
                }
                Err(e) => {
                    error!("Failed to read PDF file {}: {}", file_path.display(), e);
                    return Err(error::ErrorInternalServerError("Failed to read file"));
                }
            }
        }
    }

    warn!("PDF not found: {}", uuid);
    Err(error::ErrorNotFound("PDF file not found"))
}

/// Get picture file by UUID
pub async fn get_picture(
    uuid: web::Path<Uuid>,
    picture_path: web::Data<PathBuf>,
) -> Result<HttpResponse> {
    let uuid = uuid.into_inner();
    info!("Request for picture: {}", uuid);

    // Try common image extensions
    let extensions = ["png", "jpg", "jpeg", "gif", "webp", "bmp"];

    for ext in extensions.iter() {
        let file_path = picture_path.join(format!("{}.{}", uuid, ext));

        if file_path.exists() {
            info!("Found picture file: {}", file_path.display());

            // Guess MIME type from extension
            let mime_type = mime_guess::from_path(&file_path)
                .first_or_octet_stream()
                .to_string();

            match std::fs::read(&file_path) {
                Ok(contents) => {
                    return Ok(HttpResponse::Ok().content_type(mime_type).body(contents));
                }
                Err(e) => {
                    error!("Failed to read picture file {}: {}", file_path.display(), e);
                    return Err(error::ErrorInternalServerError("Failed to read file"));
                }
            }
        }
    }

    warn!("Picture not found: {}", uuid);
    Err(error::ErrorNotFound("Picture file not found"))
}
