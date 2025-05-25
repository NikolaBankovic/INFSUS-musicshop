package hr.fer.infsus.repository;

import hr.fer.infsus.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
