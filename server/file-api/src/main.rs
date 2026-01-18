use actix_web::{middleware, web, App, HttpServer};
use log::info;

mod config;
mod handlers;

use config::Config;
use handlers::{get_pdf, get_picture, health_check};

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    env_logger::init_from_env(env_logger::Env::default().default_filter_or("info"));

    let config = Config::from_env();

    info!("Starting File API Server");
    info!("PDF storage path: {}", config.pdf_path.display());
    info!("Picture storage path: {}", config.picture_path.display());
    info!("Server will listen on {}:{}", config.host, config.port);

    // Ensure storage directories exist
    std::fs::create_dir_all(&config.pdf_path)?;
    std::fs::create_dir_all(&config.picture_path)?;

    let pdf_path = config.pdf_path.clone();
    let picture_path = config.picture_path.clone();
    let host = config.host.clone();
    let port = config.port;

    HttpServer::new(move || {
        App::new()
            .wrap(middleware::Logger::default())
            .app_data(web::Data::new(pdf_path.clone()))
            .app_data(web::Data::new(picture_path.clone()))
            .route("/health", web::get().to(health_check))
            .route("/pdf/{uuid}", web::get().to(get_pdf))
            .route("/picture/{uuid}", web::get().to(get_picture))
    })
    .bind((host.as_str(), port))?
    .run()
    .await
}
