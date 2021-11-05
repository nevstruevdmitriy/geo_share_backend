package core.data_base

import core.data_base.datasource.Datasource
import core.geo.Point

object DBReader {
    val connection = Datasource.connectionPool.getConnection

    def sendGeoPoint(userId: String, latitude: Double, longitude: Double) = {
        val statement = connection.createStatement()
        
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS points (user_id varchar, latitude float, longitude float, time_created timestamp)")
        statement.executeUpdate(s"INSERT INTO points VALUES ('$userId', $latitude, $longitude, now())") // fix sql injection
    }

    def readGeoPoints() = {
        val statement = connection.createStatement()
        val results = statement.executeQuery("SELECT user_id, latitude, longitude, time_created FROM points")
        var out: String = ""

        while (results.next()) {
            val point = new Point(results);
            out += point.toString()
        }

        out
    }
}
