package coopa.project.domain.items.service;

import coopa.project.domain.items.Items;
import coopa.project.domain.items.repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemsService {
    private final ItemsRepository itemsRepository;

    @Transactional
    public Items getOne(int itemId) throws Exception {
        return itemsRepository.findById(itemId)
                .orElseThrow();
    }

    @Transactional
    public List<Items> getAll() {
        return itemsRepository.findAll();
    }
}
