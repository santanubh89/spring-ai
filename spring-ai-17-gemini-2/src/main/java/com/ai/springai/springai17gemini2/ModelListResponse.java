package com.ai.springai.springai17gemini2;

import java.util.List;

public record ModelListResponse(String object, List<GeminiModel> data) {
}
