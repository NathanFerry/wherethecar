use std::env;
use std::path::PathBuf;

pub struct Config {
    pub host: String,
    pub port: u16,
    pub pdf_path: PathBuf,
    pub picture_path: PathBuf,
}

impl Config {
    pub fn from_env() -> Self {
        let host = env::var("FILE_API_HOST").unwrap_or_else(|_| "0.0.0.0".to_string());
        let port = env::var("FILE_API_PORT")
            .unwrap_or_else(|_| "8080".to_string())
            .parse()
            .expect("FILE_API_PORT must be a valid u16");

        let pdf_path = env::var("PDF_STORAGE_PATH")
            .unwrap_or_else(|_| "./storage/pdf".to_string())
            .into();

        let picture_path = env::var("PICTURE_STORAGE_PATH")
            .unwrap_or_else(|_| "./storage/pictures".to_string())
            .into();

        Config {
            host,
            port,
            pdf_path,
            picture_path,
        }
    }
}
