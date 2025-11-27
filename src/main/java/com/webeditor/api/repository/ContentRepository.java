package com.webeditor.api.repository;

import com.webeditor.api.entity.Content;
import com.webeditor.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Content entity operations.
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    /**
     * Find all contents by author.
     *
     * @param author   the author
     * @param pageable pagination information
     * @return page of contents
     */
    Page<Content> findByAuthor(User author, Pageable pageable);

    /**
     * Find all contents by status.
     *
     * @param status   the status
     * @param pageable pagination information
     * @return page of contents
     */
    Page<Content> findByStatus(String status, Pageable pageable);

    /**
     * Find all contents by author and status.
     *
     * @param author   the author
     * @param status   the status
     * @param pageable pagination information
     * @return page of contents
     */
    Page<Content> findByAuthorAndStatus(User author, String status, Pageable pageable);

    /**
     * Find all contents by title containing keyword.
     *
     * @param keyword  the keyword to search in title
     * @param pageable pagination information
     * @return page of contents
     */
    Page<Content> findByTitleContaining(String keyword, Pageable pageable);

    /**
     * Count contents by author.
     *
     * @param author the author
     * @return count of contents
     */
    long countByAuthor(User author);
}
