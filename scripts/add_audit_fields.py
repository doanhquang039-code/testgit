import os
import re

directory = r"d:\Users\admoi\Desktop\SpringBoot\hr-management-system\src\main\java\com\example\hr\models"

for filename in os.listdir(directory):
    if not filename.endswith(".java"): continue
    
    filepath = os.path.join(directory, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
        
    has_created = re.search(r'\b(createdAt|created_at|creationTime)\b', content)
    has_updated = re.search(r'\b(updatedAt|updated_at|lastModified)\b', content)
    
    if has_created and has_updated:
        continue

    # Import statements
    if "java.time.LocalDateTime" not in content:
        content = re.sub(r'(package .+;\n)', r'\1\nimport java.time.LocalDateTime;\n', content, 1)
    if "jakarta.persistence.PrePersist" not in content and "@PrePersist" not in content:
        content = re.sub(r'(package .+;\n)', r'\1\nimport jakarta.persistence.PrePersist;\n', content, 1)
    if "jakarta.persistence.PreUpdate" not in content and "@PreUpdate" not in content:
        content = re.sub(r'(package .+;\n)', r'\1\nimport jakarta.persistence.PreUpdate;\n', content, 1)
    if "jakarta.persistence.Column" not in content and "@Column" not in content:
         content = re.sub(r'(package .+;\n)', r'\1\nimport jakarta.persistence.Column;\n', content, 1)

    fields_to_add = "\n"
    if not has_created:
        fields_to_add += '    @Column(name = "created_at", updatable = false)\n    private LocalDateTime createdAt;\n\n'
    if not has_updated:
        fields_to_add += '    @Column(name = "updated_at")\n    private LocalDateTime updatedAt;\n\n'

    methods_to_add = ""
    # Handle PrePersist
    if "@PrePersist" in content:
        if not has_created:
            content = re.sub(r'(@PrePersist\s+protected void \w+\(\)\s*\{)', r'\1\n        createdAt = LocalDateTime.now();', content, 1)
        if not has_updated:
            content = re.sub(r'(@PrePersist\s+protected void \w+\(\)\s*\{)', r'\1\n        updatedAt = LocalDateTime.now();', content, 1)
    else:
        methods_to_add += "    @PrePersist\n    protected void onCreate() {\n"
        if not has_created:
            methods_to_add += "        createdAt = LocalDateTime.now();\n"
        methods_to_add += "        updatedAt = LocalDateTime.now();\n    }\n\n"

    # Handle PreUpdate
    if "@PreUpdate" in content:
        if not has_updated:
             content = re.sub(r'(@PreUpdate\s+protected void \w+\(\)\s*\{)', r'\1\n        updatedAt = LocalDateTime.now();', content, 1)
    else:
        methods_to_add += "    @PreUpdate\n    protected void onUpdate() {\n        updatedAt = LocalDateTime.now();\n    }\n"
             
    last_brace_idx = content.rfind('}')
    if last_brace_idx != -1:
        content = content[:last_brace_idx] + fields_to_add + methods_to_add + content[last_brace_idx:]
    
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(content)
        
    print(f"Updated {filename}")
