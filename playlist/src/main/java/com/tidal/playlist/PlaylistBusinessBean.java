package com.tidal.playlist;

import com.google.inject.Inject;
import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;
import com.tidal.playlist.exception.PlaylistException;

import java.util.*;

/**
 * @author: eivind.hognestad@wimpmusic.com
 * Date: 15.04.15
 * Time: 12.45
 */
public class PlaylistBusinessBean {

	private PlaylistDaoBean playlistDaoBean;
	private final int MAX_TRACK = 500;


	@Inject
	public PlaylistBusinessBean(PlaylistDaoBean playlistDaoBean){
		this.playlistDaoBean = playlistDaoBean;
	}

	List<PlayListTrack> addTracks(String uuid, int userId, List<Track> tracksToAdd, int toIndex,
			Date lastUpdated) throws PlaylistException {

		try {

			TrackPlayList playList = playlistDaoBean.getPlaylistByUUID(uuid, userId);

			//We do not allow > 500 tracks in new playlists
			if (playList.getNrOfTracks() + tracksToAdd.size() > MAX_TRACK) {
				throw new PlaylistException("Playlist cannot have more than " + MAX_TRACK + " tracks");
			}

			// The index is out of bounds, put it in the end of the list.
			toIndex = checkIndexOutOfBounds(toIndex,playList);

			if (!validateIndex(toIndex, playList.getNrOfTracks())) {
				return Collections.EMPTY_LIST;
			}

			// Converts a Set of PlayListTrack to a List of PlayListTrack
			List<PlayListTrack> original = convertOriginalTrackList(playList.getPlayListTracks());

			List<PlayListTrack> added = new ArrayList<PlayListTrack>(tracksToAdd.size());

			for (Track track : tracksToAdd) {
				//Set values to playlistTrack
				PlayListTrack playlistTrack = setPlayListTrackValues(track, playList, lastUpdated);
				playList.setDuration(addTrackDurationToPlaylist(playList, track));
				original.add(toIndex++, playlistTrack);
				added.add(playlistTrack);
			}

			//set index on tracks
			original = setIndexOnTracks(original);

			playList.getPlayListTracks().clear();
			playList.getPlayListTracks().addAll(original);
			playList.setNrOfTracks(original.size());

			return added;

		} catch (Exception e) {
			e.printStackTrace();
			throw new PlaylistException("Generic error");
		}
	}

	public Track deleteTrack(String uuid, int userId, Track trackToDelete) {
		try {
			TrackPlayList playList = playlistDaoBean.getPlaylistByUUID(uuid, userId);

			// Converts a Set of PlayListTrack to a List of PlayListTrack
			List<PlayListTrack> original = convertOriginalTrackList(playList.getPlayListTracks());

			for (PlayListTrack playListTrack : original) {
				if (playListTrack.getTrack().equals(trackToDelete)) {
					Track tmp = playListTrack.getTrack();
					//removes playlist with the track
					original.remove(playListTrack);
					playList.getPlayListTracks().clear();
					playList.getPlayListTracks().addAll(original);
					playList.setNrOfTracks(original.size());
					return tmp;
				}
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new PlaylistException("Generic error");
		}
	}

	private boolean validateIndex(int toIndex, int length) {
		return toIndex >= 0 && toIndex <= length;
	}

	private float addTrackDurationToPlaylist(TrackPlayList playList, Track track) {
		return (track != null ? track.getDuration() : 0)
				+ (playList != null && playList.getDuration() != null ? playList.getDuration() : 0);
	}


	/**
	 * Check if the index is out of bounds, put it in the end of the list
	 * @param toIndex
	 * @param playList
	 * @return
	 */
	public int checkIndexOutOfBounds(int toIndex, TrackPlayList playList) {
		if (toIndex > playList.getPlayListTracksSize() || toIndex < 0) {
			return playList.getPlayListTracksSize();
		}
		return toIndex;
	}

	/**
	 * Converts a Set of PlayListTrack to a sorted List of PlayListTrack
	 * @param originalSet
	 * @return
	 */
	public List<PlayListTrack> convertOriginalTrackList(Set<PlayListTrack> originalSet) {
		if (originalSet == null || originalSet.size() == 0) {
			return new ArrayList<PlayListTrack>();
		} else {
			ArrayList<PlayListTrack> newList = new ArrayList<PlayListTrack>(originalSet);
			Collections.sort(newList);
			return newList;
		}
	}

	/**
	 * Set method
	 * set index on tracks
	 * @param original
	 * @return
	 */
	public List<PlayListTrack> setIndexOnTracks(List<PlayListTrack> original) {
		int i = 0;
		for (PlayListTrack track : original) {
			track.setIndex(i++);
		}
		return original;
	}
	/**
	 * Set method
	 * Sets properties to PlayListTrack
	 * @param track
	 * @param playList
	 * @param lastUpdated
	 * @return
	 */
	public PlayListTrack setPlayListTrackValues(Track track, TrackPlayList playList, Date lastUpdated ) {
		PlayListTrack playlistTrack = new PlayListTrack();
		playlistTrack.setTrackPlaylist(playList);
		playlistTrack.setTrackArtistId(track.getArtistId());
		playlistTrack.setDateAdded(lastUpdated);
		playlistTrack.setTrack(track);
		return playlistTrack;
	}

}
