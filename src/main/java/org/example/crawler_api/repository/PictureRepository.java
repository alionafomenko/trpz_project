package org.example.crawler_api.repository;

import org.example.crawler_api.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PictureRepository extends JpaRepository<Picture, Long> {

}
