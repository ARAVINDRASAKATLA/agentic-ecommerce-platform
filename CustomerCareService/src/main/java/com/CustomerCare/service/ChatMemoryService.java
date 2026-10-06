package com.CustomerCare.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.CustomerCare.model.ChatMessage;

@Service
public class ChatMemoryService {

	private static final int MAX_HISTORY_TURNS = 10;
	private final Map<String, List<ChatMessage>> conversationStore = new ConcurrentHashMap<>();

	public List<ChatMessage> getHistory(String sessionId) {
		if (sessionId == null || sessionId.isBlank()) {
			return Collections.emptyList();
		}
		return conversationStore.getOrDefault(sessionId, Collections.emptyList());
	}

	public synchronized void addMessage(String sessionId, String role, String content) {
		if (sessionId == null || sessionId.isBlank() || content == null) {
			return;
		}

		List<ChatMessage> history = conversationStore.computeIfAbsent(sessionId, k -> new ArrayList<>());
		history.add(ChatMessage.builder()
				.role(role)
				.content(content)
				.timestamp(LocalDateTime.now())
				.build());

		// Retain only the most recent turns to prevent context explosion
		while (history.size() > MAX_HISTORY_TURNS) {
			history.remove(0);
		}
	}

	public void clearSession(String sessionId) {
		if (sessionId != null) {
			conversationStore.remove(sessionId);
		}
	}

	public int getActiveSessionCount() {
		return conversationStore.size();
	}
}
