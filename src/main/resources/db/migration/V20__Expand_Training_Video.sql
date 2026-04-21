-- V20: Mở rộng bảng training_video
ALTER TABLE training_video
    ADD COLUMN thumbnail_url  VARCHAR(500)  DEFAULT NULL COMMENT 'Cloudinary thumbnail URL',
    ADD COLUMN public_id      VARCHAR(300)  DEFAULT NULL COMMENT 'Cloudinary public_id để xóa',
    ADD COLUMN duration_sec   INT           DEFAULT 0   COMMENT 'Thời lượng giây',
    ADD COLUMN view_count     INT           DEFAULT 0,
    ADD COLUMN is_published   BOOLEAN       DEFAULT TRUE,
    ADD COLUMN tags           VARCHAR(500)  DEFAULT NULL COMMENT 'Comma-separated tags',
    ADD COLUMN updated_at     DATETIME      DEFAULT NULL;

CREATE INDEX idx_tv_category  ON training_video(category);
CREATE INDEX idx_tv_published ON training_video(is_published);
