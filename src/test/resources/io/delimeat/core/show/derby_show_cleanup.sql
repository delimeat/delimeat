DELETE FROM SHOW_GUIDE_SOURCE;
DELETE FROM EPISODE_RESULT;
UPDATE SHOW SET NEXT_EPISODE_ID = null, PREV_EPISODE_ID = null;
DELETE FROM EPISODE;
DELETE FROM SHOW;