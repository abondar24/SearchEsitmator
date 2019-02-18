package org.abondar.industrial.searchestimator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoCompleteResponse {

    private List<AutoCompleteSuggestion> suggestions;

    public List<AutoCompleteSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AutoCompleteSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public String toString() {
        return "AutoCompleteResponse{" +
                "suggestions=" + suggestions +
                '}';
    }
}
