Refakturering:
 - Jeg endret på kodene til de to filene PlaylistBusinessBean og PlaylistBusinessBeanTest. 
 - Jeg delte opp addTrack metoden i flere mindre metoder. 
 - Jeg lot de hjelpe-metodene være public bare så jeg kunne 
	teste dem (skal egentlig være private). 
 - deleteTrack metoden tok jeg inn brukerId og Tracken som skulle slettes som parametere.
	Jeg tolket det slik at playListTrack objektet som inneholder den bestemte tracken 
	skal også slettes.
 
Refaktureringenen er veldig subjektivt og blir aldri perfekt, 
men jeg håper dere liker løsningen min.