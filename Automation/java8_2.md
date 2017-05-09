# Java8 - 2

## Date API
* régi Date: nem voltak időzónák, mutable
* Calendar class - túl bonyolult lett, mutable, 0tól számol
**LocalDate, LocalTime!**
LocalDate:
* '.now()'					kiírja yyyy-MM-dd formában
* '.of(2017, 5, 9)'			szintén kiírja
* '.ofYearDay(2017, 159)'	
LocalTime:
* '.now()'
* '.of(13,11)'
LocalDateTime:
* '.now()'
* '.now(ZoneId.of("America/New_York"))'
* '.now(Clock.systemUTC)'
'ZonedDaateTime.now()'
immutable osztály, visszatérési értékként ad vissza olyat, pl h:
'.plusDays(int)'

## kép feldolgozás
a példában 3 osztály:
* Coordinate (x, y)
* Pixel
* PixelStore

URI = getResource
Buffered Image:
* ImageIO.read(file)
* '.getHeight', '.getWidth'
* '.getRGB(x, y)

pixelek feldolgozása:
```java
int[] rgbArray = pixels
.stream().peek(p -> p.applySepia())
.mapToInt(p -> p.toInt()).toArray();
```
27.perc