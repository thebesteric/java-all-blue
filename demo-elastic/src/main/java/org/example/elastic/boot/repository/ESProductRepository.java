package org.example.elastic.boot.repository;

import org.example.elastic.boot.entity.ESProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ESProductRepository extends ElasticsearchRepository<ESProduct, Long> {
    Page<ESProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords, Pageable pageable);
}
