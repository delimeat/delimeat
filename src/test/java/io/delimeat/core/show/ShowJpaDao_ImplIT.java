package io.delimeat.core.show;

import org.apache.derby.tools.ij;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.delimeat.core.show.Show;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/show-context.xml"})
public class ShowJpaDao_ImplIT {
	
 	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final String DATA_FILE = "/io/delimeat/core/show/derby_show_test.sql";
	private static final String CLEANUP_FILE = "/io/delimeat/core/show/derby_show_cleanup.sql";

  @Autowired
  ShowJpaDao_Impl dao;
  
  private static EmbeddedDatabase dataSource;
  private static SimpleNamingContextBuilder context;
  
  @BeforeClass
  public static void beforeClass() throws Exception{
    	dataSource = new EmbeddedDatabaseBuilder()
        						.setType(EmbeddedDatabaseType.DERBY)
        						//.setName("delimeat")
        						.build();

    
    	context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
    	context.bind("jdbc/delimeatDB", dataSource);
  }
  
  @AfterClass
  public static void afterClass(){
    	dataSource.shutdown();
    	context.deactivate();
  }
  
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

  	@Transactional
	@Test
	public void createOrUpdateShowTest() throws Exception {     	
		Episode prevEpisode = new Episode(0,"TITLE",SDF.parse("2015-10-15"),2,1,false,0,null);
		Episode nextEpisode = new Episode(0,"TITLE_TWO",SDF.parse("2000-01-01"),1,2,false,0,null);
     	Show show = new Show(0,1, "TIMEZONE", "ID1", "TITLE",true, ShowType.ANIMATED, SDF.parse("2015-10-17"), SDF.parse("2015-10-16"), false, nextEpisode, prevEpisode,true, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
     	prevEpisode.setShow(show);
     	nextEpisode.setShow(show);

		Show newShow = dao.createOrUpdate(show);

		Assert.assertNotEquals(show.toString(), newShow.toString());     	
     	Assert.assertNotEquals(0, newShow.getPreviousEpisode().getEpisodeId());
     	Assert.assertNotEquals(0, newShow.getNextEpisode().getEpisodeId());
     	
     	show.setShowId(newShow.getShowId());
     	prevEpisode.setEpisodeId(newShow.getPreviousEpisode().getEpisodeId());
     	nextEpisode.setEpisodeId(newShow.getNextEpisode().getEpisodeId());
     	Assert.assertEquals(show.toString(), newShow.toString());
	}
  
  	@Transactional
	@Test(expected=ShowConcurrencyException.class)
	public void createOrUpdateShowOptimisticLockTest() throws Exception {     	
		Episode prevEpisode = new Episode(0,"TITLE",SDF.parse("2015-10-15"),2,1,false,0,null);
		Episode nextEpisode = new Episode(0,"TITLE_TWO",SDF.parse("2000-01-01"),1,2,false,0,null);
     	Show show = new Show(0,1, "TIMEZONE", "ID1", "TITLE",true, ShowType.ANIMATED, SDF.parse("2015-10-17"), SDF.parse("2015-10-16"), false, nextEpisode, prevEpisode,true, Integer.MIN_VALUE, Integer.MAX_VALUE, 99);
     	prevEpisode.setShow(show);
     	nextEpisode.setShow(show);
		
		dao.createOrUpdate(show);
	}
  
  	@Transactional
	@Test(expected=ShowNotFoundException.class)
	public void readNotFoundShowTest() throws Exception {
		dao.read(Long.MAX_VALUE);
	}
  
  	@Transactional
  	@Test
  	public void readShowTest() throws Exception { 
		Episode prevEpisode = new Episode(2,"PREVIOUS EPISODE",SDF.parse("2016-01-01"),1,3,true,4,null);
		Episode nextEpisode = new Episode(3,"NEXT EPISODE",SDF.parse("2016-02-01"),2,1,false,3,null);
     	Show expectedShow = new Show(1,1200, "TIMEZONE", "GUIDEID", "TITLE",true, ShowType.ANIMATED, SDF.parse("1988-12-25"), null, false, nextEpisode, prevEpisode,true, 100, 101, 99);
     	prevEpisode.setShow(expectedShow);
     	nextEpisode.setShow(expectedShow);
     
     	Show show = dao.read(1);
     
     	Assert.assertEquals(expectedShow.toString(), show.toString());
   }

  	@Transactional
	@Test(expected=ShowNotFoundException.class)
	public void deleteNotFoundShowTest() throws Exception {
		dao.delete(Long.MAX_VALUE);
	}
  
  	@Transactional
	@Test
	public void deleteShowTest() throws Exception {
		dao.delete(1);
	}
}
