package edu.neu.csye7125.notifier.dao;

public interface ElasticsearchDao {

    String search(String category, String keyword);

}
