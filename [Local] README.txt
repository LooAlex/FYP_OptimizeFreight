Common Errors and Solutions:

[ERROR --> PointOutOfBoundsException]
Happens when we are using or what the system perceives as coordinates outside the osm.pbf file map boundary.

eg.
Exception in thread "AWT-EventQueue-0" java.lang.RuntimeException: [com.graphhopper.util.exceptions.PointOutOfBoundsException: 
Point 0 is out of bounds: -20.16366002209838,57.50698320195978, the bounds are: 102.256091,107.8250165,10.2748747,14.7518334

The Above exception is common when switching osm.pbf files, before i was using cambodia which have its top boundary = 10.2, bottom boundary = 14.8 and left boundary stopped at 102.3 and right boundary at 107.9,
hence bounds are : 
102.256091, --> accurate left boundary
107.8250165 --> accurate right boundary
10.2748747,--> accurate top boundary
14.7518334--> accurate bototm boundary
	
and those coords bord well with Cambodia's boundary
But im using coords in Mauritius map and thats why it is saying the bounds are in cambodia , we dont have data about those location
