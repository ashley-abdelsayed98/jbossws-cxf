/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.ws.jaxws.jbws2307;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.Test;

import org.jboss.ws.common.IOUtils;
import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestHelper;
import org.jboss.wsf.test.JBossWSTestSetup;

/**
 * [JBWS-3820] JAXWS 2.1 / 2.0 clients and WebServiceRef using JAXWS features cause NoSuchMethodException
 * 
 * @author alessio.soldano@jboss.com
 */
public class WebServiceRefMTOMTestCase extends JBossWSTest
{

   public static Test suite()
   {
      return new JBossWSTestSetup(WebServiceRefMTOMTestCase.class, DeploymentArchives.SERVER, true);
   }
   
   public void testUsingClientArchive3() throws Exception
   {
      try {
         JBossWSTestHelper.deploy(DeploymentArchives.CLIENT_3);
         assertEquals("true", IOUtils.readAndCloseStream(new URL("http://" + getServerHost() + ":8080/jaxws-jbws2307-client-3/jbws2307?mtom=true").openStream()));
         HttpURLConnection con = (HttpURLConnection)new URL("http://" + getServerHost() + ":8080/jaxws-jbws2307-client-3/jbws2307").openConnection();
         BufferedReader isr = new BufferedReader(new InputStreamReader(con.getInputStream()));
         assertEquals("true", isr.readLine());
      } finally {
         JBossWSTestHelper.undeploy(DeploymentArchives.CLIENT_3);
      }
   }
   
   public void testUsingClientArchive2() throws Exception
   {
      try {
         JBossWSTestHelper.deploy(DeploymentArchives.CLIENT_2);
         URL url = new URL("http://" + getServerHost() + ":8080/jaxws-jbws2307-client-2/jbws2307?mtom=true");
         final HttpURLConnection c = (HttpURLConnection)url.openConnection();
         c.connect();
         assertEquals(500, c.getResponseCode());
         String error = IOUtils.readAndCloseStream(c.getErrorStream());
         c.disconnect();
         assertTrue(error.contains("Could not instantiate ClientServlet2"));
      } finally {
         JBossWSTestHelper.undeploy(DeploymentArchives.CLIENT_2);
      }
   }
}