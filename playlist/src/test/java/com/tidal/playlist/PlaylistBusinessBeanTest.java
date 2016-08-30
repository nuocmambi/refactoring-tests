package com.tidal.playlist;

import com.google.inject.Inject;
import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

/**
 * @author: eivind.hognestad@wimpmusic.com
 * Date: 15.04.15
 * Time: 14.32
 */
@Guice(modules = TestBusinessModule.class)
public class PlaylistBusinessBeanTest {

	@Inject
	PlaylistBusinessBean playlistBusinessBean;

	List<Track> trackList;
	Track t1;
	Track t2;
	Track t3;

	@BeforeMethod
	public void setUp() throws Exception {
		//Create Tracks
		t1 = new Track();
		t1.setArtistId(2);
		t1.setTitle("Track 2");
		t1.setTrackNumberIdx(2);
		t1.setId(10000);

		t2 = new Track();
		t2.setArtistId(3);
		t2.setTitle("Track 3");
		t2.setTrackNumberIdx(3);
		t2.setId(30000);

		t3 = new Track();
		t3.setArtistId(5);
		t3.setTitle("Track 4");
		t3.setTrackNumberIdx(5);
		t3.setId(50000);

		//create a List with Tracks
		trackList = new ArrayList<Track>();
		trackList.add(t1);
		trackList.add(t2);
		trackList.add(t3);
	}

	@AfterMethod
	public void tearDown() throws Exception {

	}

	@Test
	public void testAddTracks() throws Exception {
		List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(UUID.randomUUID().toString(), 1, trackList, 5, new Date());

		assertTrue(playListTracks.size() > 0);
	}


	@Test
	public void testDeleteTracksEqualsNull() throws Exception {
		Track track = playlistBusinessBean.deleteTrack(UUID.randomUUID().toString(), 1, t1);
		assertNull(track);
	}

	@Test
	public void testCheckIndexEqualsZero() {
		TrackPlayList trackPlayList = new TrackPlayList();
		PlayListTrack playListTrack = new PlayListTrack();

		Set<PlayListTrack> plt = new HashSet<>();
		plt.add(playListTrack);
		trackPlayList.setPlayListTracks(plt);

		int returnValue = playlistBusinessBean.checkIndexOutOfBounds(0, trackPlayList);

		assertEquals(returnValue, 0);
	}

	@Test
	public void testCheckIndexOverBound() {
		TrackPlayList trackPlayList = new TrackPlayList();
		PlayListTrack playListTrack = new PlayListTrack();

		Set<PlayListTrack> plt = new HashSet<>();
		plt.add(playListTrack);
		trackPlayList.setPlayListTracks(plt);

		int returnValue = playlistBusinessBean.checkIndexOutOfBounds(2, trackPlayList);
		assertEquals(returnValue, trackPlayList.getPlayListTracksSize());
	}

	@Test
	public void testCheckIndexBelowBound() {
		TrackPlayList trackPlayList = new TrackPlayList();
		PlayListTrack playListTrack = new PlayListTrack();

		Set<PlayListTrack> plt = new HashSet<>();
		plt.add(playListTrack);
		trackPlayList.setPlayListTracks(plt);

		int returnValue = playlistBusinessBean.checkIndexOutOfBounds(-2, trackPlayList);

		assertEquals(returnValue, trackPlayList.getPlayListTracksSize());

	}

	@Test
	public void testConvertOriginalTrackList() {
		PlayListTrack p1 = new PlayListTrack();
		PlayListTrack p2 = new PlayListTrack();
		p1.setId(1);
		p2.setId(2);

		Set<PlayListTrack> set = new HashSet<>();
		set.add(p1);
		set.add(p2);

		ArrayList<PlayListTrack> list = (ArrayList<PlayListTrack>) playlistBusinessBean.convertOriginalTrackList(set);

		assertTrue(list.contains(p1));
		assertTrue(list.contains(p2));
	}

}
