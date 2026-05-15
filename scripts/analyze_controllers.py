import os
import re

directory = r"d:\Users\admoi\Desktop\SpringBoot\hr-management-system\src\main\java\com\example\hr\controllers"

missing_crud = []
missing_search_sort = []

for filename in os.listdir(directory):
    if not filename.endswith("Controller.java"): continue
    if filename.endswith(".bak"): continue
    
    filepath = os.path.join(directory, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
        
    has_get = "@GetMapping" in content
    has_post = "@PostMapping" in content
    has_put = "@PutMapping" in content or "PostMapping(" in content # sometimes they use POST for update
    has_delete = "@DeleteMapping" in content or "PostMapping(" in content # or POST for delete
    
    # Strictly checking for real CRUD or forms
    has_crud = has_get and has_post and ("update" in content.lower() or "edit" in content.lower()) and ("delete" in content.lower())
    
    # Check for search/sort parameters
    # E.g. @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "id") String sortBy
    has_search = "keyword" in content or "search" in content.lower()
    has_sort = "sort" in content.lower() or "Pageable" in content
    
    if not has_crud:
        missing_crud.append(filename)
    if not (has_search and has_sort):
        missing_search_sort.append(filename)

print("Controllers potentially missing complete CRUD:")
for c in missing_crud:
    print(" -", c)

print("\nControllers potentially missing Search/Sort/Pagination:")
for c in missing_search_sort:
    print(" -", c)
