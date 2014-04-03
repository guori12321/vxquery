(: XQuery Self Join Query :)
(: Self join with all stations finding the difference in min and max       :)
(: temperature and get the average.                                        :)
fn:count(
    let $sensor_collection_min := "ghcnd_quarter_1|ghcnd_quarter_2|ghcnd_quarter_3|ghcnd_quarter_4"
    for $r_min in collection($sensor_collection_min)/dataCollection/data
    
    let $sensor_collection_max := "ghcnd_quarter_1|ghcnd_quarter_2|ghcnd_quarter_3|ghcnd_quarter_4"
    for $r_max in collection($sensor_collection_max)/dataCollection/data
    
    where $r_min/station eq $r_max/station
        and $r_min/date eq $r_max/date
        and $r_min/dataType eq "TMIN"
        and $r_max/dataType eq "TMAX"
    return $r_max/value - $r_min/value
)
