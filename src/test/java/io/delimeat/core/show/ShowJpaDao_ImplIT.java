package io.delimeat.core.show;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.derby.tools.ij;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/show-context-test.xml"})
public class ShowJpaDao_ImplIT {
	
 	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final String DATA_FILE = "/io/delimeat/core/show/derby_show_test.sql";
	private static final String CLEANUP_FILE = "/io/delimeat/core/show/derby_show_cleanup.sql";

  @Autowired
  ShowJpaDao_Impl dao;
  @Autowired
  DataSource dataSource;
  
  //private static EmbeddedDatabase dataSource;
  //private static SimpleNamingContextBuilder context;
  
  
  @BeforeClass
  public static void beforeClass() throws Exception{
	  //System.setProperty("io.delimeat.core.show.driverClassName", "org.apache.derby.jdbc.EmbeddedDriver");
	  System.setProperty("io.delimeat.core.show.jdbcUrl", "jdbc:derby:memory:delimeat;create=true;");
	  /*
    	dataSource = new EmbeddedDatabaseBuilder()
        						.setType(EmbeddedDatabaseType.DERBY)
        						//.setName("delimeat")
        						.build();

    
    	context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
    	context.bind("jdbc/delimeatDB", dataSource);
    	*/
  }

  
  /*
  @AfterClass
  public static void afterClass(){
    	dataSource.shutdown();
    	context.deactivate();
  }
  */
  
  	@Before
  	public void setUp() throws Exception {
     	try(InputStream is = System.class.getResourceAsStream(DATA_FILE);){
        ij.runScript(dataSource.getConnection(), is, "UTF-8", System.out, "UTF-8");
      }
   }
  
  	@After
  	public void tearDown() throws Exception {
		try(InputStream is = System.class.getResourceAsStream(CLEANUP_FILE);){
			ij.runScript(dataSource.getConnection(), is, "UTF-8", System.out, "UTF-8");
      }
   }

  	@Ignore
  	@Transactional
	@Test
	public void createOrUpdateShowTest() throws Exception {     	
     	Show show = new Show(0,1, "TIMEZONE", "ID1", "TITLE",true, ShowType.ANIMATED, SDF.parse("2015-10-17"),SDF.parse("2015-10-15"), SDF.parse("2015-10-16"),SDF.parse("2015-10-14"), false,true, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
		Episode prevEpisode = new Episode(0,"TITLE",SDF.parse("2015-10-15"),2,1,false,EpisodeStatus.FOUND,0,show);
     	show.setPreviousEpisode(prevEpisode);
		Episode nextEpisode = new Episode(0,"TITLE_TWO",SDF.parse("2000-01-01"),1,2,false,EpisodeStatus.FOUND,0,show);
     	show.setNextEpisode(nextEpisode);

		Show newShow = dao.createOrUpdate(show);

		Assert.assertNotEquals(show.toString(), newShow.toString());     	
     	Assert.assertNotEquals(0, newShow.getPreviousEpisode().getEpisodeId());
     	Assert.assertNotEquals(0, newShow.getNextEpisode().getEpisodeId());
     	
     	show.setShowId(newShow.getShowId());
     	prevEpisode.setEpisodeId(newShow.getPreviousEpisode().getEpisodeId());
     	nextEpisode.setEpisodeId(newShow.getNextEpisode().getEpisodeId());
     	Assert.assertEquals(show.toString(), newShow.toString());
	}
  
  	@Ignore
  	@Transactional
	@Test(expected=ShowConcurrencyException.class)
	public void createOrUpdateShowOptimisticLockTest() throws Exception {     	
     	Show show = new Show(0,1, "TIMEZONE", "ID1", "TITLE",true, ShowType.ANIMATED, SDF.parse("2015-10-17"),SDF.parse("2015-10-15"), SDF.parse("2015-10-16"),SDF.parse("2015-10-14"), false,true, Integer.MIN_VALUE, Integer.MAX_VALUE, 99);
		Episode prevEpisode = new Episode(0,"TITLE",SDF.parse("2015-10-15"),2,1,false,EpisodeStatus.FOUND,0,show);
     	show.setPreviousEpisode(prevEpisode);
		Episode nextEpisode = new Episode(0,"TITLE_TWO",SDF.parse("2000-01-01"),1,2,false,EpisodeStatus.FOUND,0,show);
     	show.setNextEpisode(nextEpisode);
		
		dao.createOrUpdate(show);
	}
  
  	@Ignore
  	@Transactional
	@Test(expected=ShowNotFoundException.class)
	public void readNotFoundShowTest() throws Exception {
		dao.read(Long.MAX_VALUE);
	}
  
  	@Ignore
  	@Transactional
  	@Test
  	public void readShowTest() throws Exception { 

     	Show expectedShow = new Show(1,1200, "TIMEZONE", "GUIDEID", "TITLE",true, ShowType.ANIMATED, SDF.parse("1988-12-25"),SDF.parse("1988-12-24") , null, null, false,true, 100, 101, 99);
		Episode prevEpisode = new Episode(2,"PREVIOUS EPISODE",SDF.parse("2016-01-01"),1,3,true,EpisodeStatus.FOUND,4,expectedShow);
     	expectedShow.setPreviousEpisode(prevEpisode);
		Episode nextEpisode = new Episode(3,"NEXT EPISODE",SDF.parse("2016-02-01"),2,1,false,EpisodeStatus.FOUND,3,expectedShow);
     	expectedShow.setNextEpisode(nextEpisode);
     
     	Show show = dao.read(1);
     
     	Assert.assertEquals(expectedShow.toString(), show.toString());
   }

  	@Ignore
  	@Transactional
	@Test(expected=ShowNotFoundException.class)
	public void deleteNotFoundShowTest() throws Exception {
		dao.delete(Long.MAX_VALUE);
	}
  
  	@Ignore
  	@Transactional
	@Test
	public void deleteShowTest() throws Exception {
		dao.delete(1);
	}
}
