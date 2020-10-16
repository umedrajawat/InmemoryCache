package com.kdu.assignment3.jpaclasses;

import javax.persistence.*;

/**
 * The class used to store the values in the database.
 * The NamedQueries which need to be used are mentioned here.
 * The table name is mentioned via annotation since the class name is different.
 *
 * The class has the fields as the fields in the database.
 * There are respective getter and setter methods.
 */
@Entity
@Table(name = "latitudelongitude")
@NamedQueries({
         @NamedQuery(name="findByAddress",query="SELECT l FROM LatLongAddr l WHERE address = :address"),
         @NamedQuery(name = "findByTimesAccessed",query = "SELECT l FROM LatLongAddr l ORDER BY times_accessed DESC")
        })
public class LatLongAddr {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   public void setId(int id) {
      this.id = id;
   }

   public int getId() {
      return id;
   }

   private Float latitude;
   private Float longitude;
   private String address;
   private int times_accessed;

   public int getTimes_accessed() {
      return times_accessed;
   }

   public void setTimesAccessed(int times_accessed) {
      this.times_accessed = times_accessed;
   }

   public Float getLatitude() {
      return latitude;
   }

   public void setLatitude(Float latitude) {
      this.latitude = latitude;
   }

   public Float getLongitude() {
      return longitude;
   }

   public void setLongitude(Float longitude) {
      this.longitude = longitude;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }
}
