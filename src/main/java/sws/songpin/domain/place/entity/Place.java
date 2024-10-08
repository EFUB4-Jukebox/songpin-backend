package sws.songpin.domain.place.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sws.songpin.domain.pin.entity.Pin;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", updatable = false)
    private Long placeId;

    @Column(name = "place_name", length = 100)
    @NotNull
    private String placeName;

    @Column(name = "address")
    @NotNull
    private String address;

    @Column(name = "provider_address_id")
    @NotNull
    private Long providerAddressId;

    @Column(name = "latitude")
    @NotNull
    private double latitude;

    @Column(name = "longitude")
    @NotNull
    private double longitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Place(Long placeId, String placeName, String address, Long providerAddressId,
                 double latitude, double longitude) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.address = address;
        this.providerAddressId = providerAddressId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pins = new ArrayList<>();
    }
}
