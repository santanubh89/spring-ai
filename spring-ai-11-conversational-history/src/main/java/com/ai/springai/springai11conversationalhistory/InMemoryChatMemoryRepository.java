package com.ai.springai.springai11conversationalhistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryChatMemoryRepository implements ChatMemoryRepository {

    public static final Logger log = LoggerFactory.getLogger(InMemoryChatMemoryRepository.class);

    Map<String, List<Message>> chatMemoryStore = new ConcurrentHashMap<>();

    @Override
    public List<String> findConversationIds() {
        ArrayList<String> conversationIds = new ArrayList<>(chatMemoryStore.keySet());
        log.info("findConversationIds::conversationIds {}", conversationIds);
        return conversationIds;
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        List<Message> messages = chatMemoryStore.get(conversationId);
        log.info("findByConversationId::conversationId: {}, messages: {}", conversationId, messages);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        List<Message> existingMessage = chatMemoryStore.get(conversationId);
        if (null != existingMessage) {
            messages.addAll(existingMessage);
        }
        log.info("saveAll::conversationId {}, messages {}", conversationId, messages);
        chatMemoryStore.put(conversationId, messages);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        log.info("deleteByConversationId::conversationId {}", conversationId);
        chatMemoryStore.remove(conversationId);
    }
}
