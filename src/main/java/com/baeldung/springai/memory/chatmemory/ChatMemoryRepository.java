package com.baeldung.springai.memory.chatmemory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface ChatMemoryRepository extends JpaRepository<ChatMemoryEntity, Long> {
    List<ChatMemoryEntity> findByUserIdAndConversationIdOrderByCreatedAtAsc(Long userId, String conversationId);
    List<ChatMemoryEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndConversationId(Long userId, String conversationId);

}
