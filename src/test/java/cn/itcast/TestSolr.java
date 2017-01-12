package cn.itcast;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {

	@Test
	public void testAdd() throws Exception{
		String baseURL = "http://192.168.40.133:8080/solr";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 1);
		doc.setField("name", "瑜伽服");
		solrServer.add(doc);
		solrServer.commit();
	}
	@Test
	public void select() throws Exception{
		String baseURL = "http://192.168.40.133:8080/solr";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		SolrQuery params = new SolrQuery();
		params.set("q", "*:*");
		QueryResponse response = solrServer.query(params);
		SolrDocumentList documentList = response.getResults();
		long count = documentList.getNumFound();
		System.out.println("总条数======="+count);
		for (SolrDocument solrDocument : documentList) {
			String id = (String) solrDocument.get("id");
			System.out.println("id为====="+id);
		}
	}
}
