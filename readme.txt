Refakturering:
 - Jeg endret p� kodene til de to filene PlaylistBusinessBean og PlaylistBusinessBeanTest. 
 - Jeg delte opp addTrack metoden i flere mindre metoder. 
 - Jeg lot de hjelpe-metodene v�re public bare s� jeg kunne 
	teste dem (skal egentlig v�re private). 
 - deleteTrack metoden tok jeg inn brukerId og Tracken som skulle slettes som parametere.
	Jeg tolket det slik at playListTrack objektet som inneholder den bestemte tracken 
	skal ogs� slettes.
 
Refaktureringenen er veldig subjektivt og blir aldri perfekt, 
men jeg h�per dere liker l�sningen min.