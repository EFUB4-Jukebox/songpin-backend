package sws.songpin.entity.place.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.global.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", updatable = false)
    private Long placeId;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Place(Long placeId, String placeName, String address, double latitude, double longitude) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pins = new ArrayList<>();
    }
}
