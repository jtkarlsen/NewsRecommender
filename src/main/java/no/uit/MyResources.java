
package no.uit;

import no.uit.Beans.Article;
import no.uit.Beans.ArticleList;
import no.uit.Beans.Group;
import no.uit.utils.Utils;
import no.uit.utils.WordCount;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boostingQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/")
public class MyResources {
    
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @Path("/groups/{user}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroups(@PathParam("user") String username) throws Exception {
        MySQLConnector connector = new MySQLConnector();
        List<Group> groups = connector.getGroupsForUser(username);
        return groups;
    }

    @Path("/articles/{id}")
    @GET
//    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt(@PathParam("id") int groupId) throws Exception {
        // Fetch keywords for group
        MySQLConnector connector = new MySQLConnector();
        List<String> keywords = connector.getKeywordsForGroup(groupId);
        List<WordCount> wordCountList = Utils.stringArrayCounter(keywords);

        // Build query
        // TODO: add term age
        BoolQueryBuilder qb = QueryBuilders
                .boolQuery();
        for (WordCount wordCount : wordCountList) {
            if (wordCount.getCount() > 1) {
                qb.should(multiMatchQuery(wordCount.getWord(), "title", "description").boost(wordCount.getCount()));
            }
        }

        // Setup client
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "elasticsearch-jka055").build();
        Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(
                "localhost",
                9300));
        // Search
        SearchResponse response = client.prepareSearch("messages")
                .setTypes("message")
                .setQuery(qb)
                .setSize(50)
                .execute()
                .actionGet();
        client.close();

        // Handle response
        ArticleList articles = new ArticleList(response.getTook().toString(), response.getHits().totalHits(), response.getHits().hits().length);
        for (SearchHit hit : response.getHits().getHits()) {
            Article article = new Article();
            article.setId(hit.getId());
            article.setScore(hit.getScore());

            Map<String,Object> result = hit.getSource();

            article.setTitle(result.get("title").toString());
            article.setDescription(result.get("description").toString());
            article.setLink(result.get("link").toString());
            article.setAuthor(result.get("author").toString());
            article.setGuid(result.get("guid").toString());
            article.setPubDate(result.get("pubDate").toString());
            articles.addArticles(article);
        }

        // Return response as json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(articles);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
}
