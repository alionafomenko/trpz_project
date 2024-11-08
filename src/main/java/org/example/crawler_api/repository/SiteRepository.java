package org.example.crawler_api.repository;

import org.example.crawler_api.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query(value = "SELECT * FROM trpz.get_all_sites()", nativeQuery = true)
    List<Site> getAllSites();

    @Query(value = "SELECT * FROM trpz.add_site(?1, ?2)", nativeQuery = true)
    String addSite(String url, String title);

    boolean existsByTitle(String title);

}
