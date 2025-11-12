package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.Mess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository for Mess
 *
 */
@Repository
public interface MessRepository extends JpaRepository<Mess, Integer> {
    // Other mess-specific queries can go here
    @Query(value = """
            SELECT m.mess_id,
                   m.mess_name,
                   m.latitude,
                   m.longitude,
                   ST_Distance_Sphere(
                     POINT(:lng, :lat),
                     POINT(m.longitude, m.latitude)
                   ) AS distance_meters
            FROM tbl_Mess m
            INNER JOIN tbl_User u ON m.user_id = u.user_id
            WHERE u.active = true
              AND m.latitude IS NOT NULL
              AND m.longitude IS NOT NULL
              AND m.latitude BETWEEN :latMin AND :latMax
              AND m.longitude BETWEEN :lngMin AND :lngMax
            HAVING distance_meters <= :radius
            ORDER BY distance_meters
            """, nativeQuery = true)
    List<Object[]> findNearbyActiveWithDistanceSpatial(
            @Param("lng") double longitude,
            @Param("lat") double latitude,
            @Param("latMin") double latMin,
            @Param("latMax") double latMax,
            @Param("lngMin") double lngMin,
            @Param("lngMax") double lngMax,
            @Param("radius") double radius
    );

    @Query(value = """
            SELECT m.mess_id, m.mess_name, m.latitude, m.longitude,
              ( 6371000 * acos(
                  cos(radians(:lat)) * cos(radians(m.latitude))
                  * cos(radians(m.longitude) - radians(:lng))
                  + sin(radians(:lat)) * sin(radians(m.latitude))
                )
              ) AS distance_meters
            FROM tbl_Mess m
            INNER JOIN tbl_User u ON m.user_id = u.user_id
            WHERE u.active = true
              AND m.latitude IS NOT NULL
              AND m.longitude IS NOT NULL
              AND m.latitude BETWEEN :latMin AND :latMax
              AND m.longitude BETWEEN :lngMin AND :lngMax
            HAVING distance_meters <= :radius
            ORDER BY distance_meters
            """, nativeQuery = true)
    List<Object[]> findNearbyActiveWithDistanceHaversine(
            @Param("lng") double longitude,
            @Param("lat") double latitude,
            @Param("latMin") double latMin,
            @Param("latMax") double latMax,
            @Param("lngMin") double lngMin,
            @Param("lngMax") double lngMax,
            @Param("radius") double radius
    );

}
