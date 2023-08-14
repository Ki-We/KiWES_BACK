package server.api.kiwes.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.category.type.CategoryType;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(CategoryType type);
}
