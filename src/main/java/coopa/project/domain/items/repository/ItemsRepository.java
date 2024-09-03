package coopa.project.domain.items.repository;

import coopa.project.domain.items.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items, Integer> {
    Items findByItemName(String itemName);
}
