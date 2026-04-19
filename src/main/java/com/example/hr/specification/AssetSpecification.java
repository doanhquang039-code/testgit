package com.example.hr.specification;

import com.example.hr.enums.AssetStatus;
import com.example.hr.models.CompanyAsset;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA Specification cho dynamic query company assets.
 */
public final class AssetSpecification {

    private AssetSpecification() {}

    public static Specification<CompanyAsset> hasStatus(AssetStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<CompanyAsset> hasCategory(String category) {
        return (root, query, cb) -> {
            if (category == null || category.isBlank()) return cb.conjunction();
            return cb.equal(root.get("category"), category);
        };
    }

    public static Specification<CompanyAsset> nameContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("assetName")), pattern),
                    cb.like(cb.lower(root.get("assetCode")), pattern)
            );
        };
    }
}
