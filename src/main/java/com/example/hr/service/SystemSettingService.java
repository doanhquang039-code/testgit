package com.example.hr.service;

import com.example.hr.models.SystemSetting;
import com.example.hr.repository.SystemSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SystemSettingService {

    private final SystemSettingRepository settingRepository;

    public SystemSettingService(SystemSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @PostConstruct
    public void initDefaultSettings() {
        if (settingRepository.count() == 0) {
            addSetting("COMPANY_NAME", "Công ty CP Công nghệ HRMS", "Tên công ty hiển thị trên báo cáo");
            addSetting("WORKING_HOURS_PER_DAY", "8", "Số giờ làm việc tiêu chuẩn mỗi ngày");
            addSetting("MAX_LEAVE_CARRYOVER", "5", "Số ngày phép tối đa được chuyển sang năm sau");
            addSetting("ENABLE_AUTO_ATTENDANCE", "true", "Tự động chấm công vắng mặt nếu không check-in (true/false)");
        }
    }

    public List<SystemSetting> getAllSettings() {
        return settingRepository.findAll();
    }

    public String getValue(String key, String defaultValue) {
        return settingRepository.findById(key)
                .map(SystemSetting::getSettingValue)
                .orElse(defaultValue);
    }

    public void updateSetting(String key, String value) {
        SystemSetting setting = settingRepository.findById(key).orElse(new SystemSetting());
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setUpdatedAt(LocalDateTime.now());
        settingRepository.save(setting);
    }

    public void addSetting(String key, String value, String description) {
        if (!settingRepository.existsById(key)) {
            SystemSetting setting = new SystemSetting();
            setting.setSettingKey(key);
            setting.setSettingValue(value);
            setting.setDescription(description);
            setting.setUpdatedAt(LocalDateTime.now());
            settingRepository.save(setting);
        }
    }
}