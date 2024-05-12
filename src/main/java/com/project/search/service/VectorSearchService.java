package com.project.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.project.search.model.SearchResult; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VectorSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<SearchResult> vectorSearch(float[] queryVector, float imageWeight, float nameWeight, float textWeight, int amount) throws IOException {
        SearchResponse<Map> response = elasticsearchClient.search(s -> s
            .index("meme_vector")
            .query(q -> q
                    .scriptScore(ss -> ss
                            .query(qq -> qq.matchAll(_ma -> _ma))
                            .script(sc -> sc
                                    .inline(i -> i
                                            .lang("painless")
                                            .source(
                                                    "cosineSimilarity(params.queryVector, 'image_vector') * params.imageWeight + " +
                                                    "cosineSimilarity(params.queryVector, 'name_vector') * params.nameWeight + " +
                                                    "cosineSimilarity(params.queryVector, 'text_vector') * params.textWeight + 1.0"
                                            )
                                            .params(Map.of(
                                                    "queryVector", JsonData.of(queryVector),
                                                    "imageWeight", JsonData.of(imageWeight),
                                                    "nameWeight", JsonData.of(nameWeight),
                                                    "textWeight", JsonData.of(textWeight)
                                            ))
                                    )
                            )
                    )
            )
            .size(amount), Map.class);

        return response.hits().hits().stream()
                .map(hit -> new SearchResult(
                        hit.source().get("image_url").toString(), //return url
                        hit.score() //return similarity
                ))
                .collect(Collectors.toList());
    }

    public List<SearchResult> combineSearch(String searchText, float[] queryVector, float textBoost, float vectorBoost, int amount) throws IOException {
        float modifiedTextBoost = textBoost * 0.01f;
        SearchResponse<Map> response = elasticsearchClient.search(s -> s
            .index("meme_vector")
            .query(q -> q
                .bool(b -> b
                    .should(sh1 -> sh1
                        .multiMatch(mm -> mm
                            .query(searchText)
                            .fields(List.of("name", "text"))
                            .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BestFields)
                            .tieBreaker(0.3)
                            .boost(modifiedTextBoost)
                        )
                    )
                    .should(sh2 -> sh2
                        .scriptScore(ss -> ss
                            .query(qq -> qq.matchAll(ma -> ma))
                            .script(sc -> sc
                                .inline(i -> i
                                    .lang("painless")
                                    .source("cosineSimilarity(params.queryVector, 'image_vector') * params.vectorBoost + 1.0")
                                    .params(Map.of(
                                        "queryVector", JsonData.of(queryVector),
                                        "vectorBoost", JsonData.of(vectorBoost)
                                    ))
                                )
                            )
                        )
                    )
                )
            )
            .size(amount), Map.class);
    
        return response.hits().hits().stream()
                .map(hit -> new SearchResult(
                        hit.source().get("image_url").toString(), // Return URL
                        hit.score() // Return similarity
                ))
                .collect(Collectors.toList());
    }
}
