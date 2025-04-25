package Agents;

import Agents.DAO.trio;
import java.util.List;

public interface IStatistique {
    List<trio<String, String, Integer>> getStats();
}