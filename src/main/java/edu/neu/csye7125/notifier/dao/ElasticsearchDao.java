package edu.neu.csye7125.notifier.dao;

import java.util.List;
import java.util.Map;

public interface ElasticsearchDao {

    List<Map<String, String>> search(String category, String keyword);

}
