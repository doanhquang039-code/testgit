CREATE TABLE training_video (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    video_url VARCHAR(500) NOT NULL,
    category VARCHAR(100),
    uploader_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_video_uploader FOREIGN KEY (uploader_id) REFERENCES user(id)
);

-- Quan trọng: Ghi dữ liệu xuống DB
COMMIT;