/*
 * Copyright (c)  2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.extension.siddhi.execution.json;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

import java.util.concurrent.atomic.AtomicInteger;

public class GetFloatJSONFunctionTestCase {
    private static final Logger log = Logger.getLogger(GetFloatJSONFunctionTestCase.class);
    private AtomicInteger count = new AtomicInteger(0);
    private static final String JSON_INPUT = "{name:\"John\", age:25, citizen:false}";

    @BeforeMethod
    public void init() {
        count.set(0);
    }

    @Test
    public void testGetFloatFromJSON() throws InterruptedException {
        log.info("GetFloatJSONFunctionTestCase - testGetFloatFromJSON");
        SiddhiManager siddhiManager = new SiddhiManager();
        String stream = "define stream InputStream(json string,path string);\n";
        String query = ("@info(name = 'query1')\n" +
                "from InputStream\n" +
                "select json:getFloat(json,path) as married\n" +
                "insert into OutputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(stream + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents,
                                Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    count.incrementAndGet();
                    switch (count.get()) {
                        case 1:
                            AssertJUnit.assertEquals(new Float(25.0), event.getData(0));
                            break;
                        case 2:
                            AssertJUnit.assertEquals(null, event.getData(0));
                            break;
                        case 3:
                            AssertJUnit.assertEquals(null, event.getData(0));
                            break;
                        case 4:
                            AssertJUnit.assertEquals(null, event.getData(0));
                            break;
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("InputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{JSON_INPUT, "$.age"});
        inputHandler.send(new Object[]{JSON_INPUT, "$.citizen"});
        inputHandler.send(new Object[]{JSON_INPUT, "$.name"});
        inputHandler.send(new Object[]{JSON_INPUT, "$.married"});
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void testGetFloatFromJSONWithObjectInput() throws InterruptedException, ParseException {
        log.info("GetFloatJSONFunctionTestCase - testGetFloatFromJSONWithObjectInput");
        SiddhiManager siddhiManager = new SiddhiManager();
        String stream = "define stream InputStream(json object,path string);\n";
        String query = ("@info(name = 'query1')\n" +
                "from InputStream\n" +
                "select json:getFloat(json,path) as married\n" +
                "insert into OutputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(stream + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents,
                                Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    count.incrementAndGet();
                    switch (count.get()) {
                        case 1:
                            AssertJUnit.assertEquals(new Float(25.0), event.getData(0));
                            break;
                        case 2:
                            AssertJUnit.assertEquals(null, event.getData(0));
                            break;
                        case 3:
                            AssertJUnit.assertEquals(null, event.getData(0));
                            break;
                        case 4:
                            AssertJUnit.assertEquals(null, event.getData(0));
                            break;
                    }
                }
            }
        });
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(JSON_INPUT);
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("InputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{jsonObject, "$.age"});
        inputHandler.send(new Object[]{jsonObject, "$.citizen"});
        inputHandler.send(new Object[]{jsonObject, "$.name"});
        inputHandler.send(new Object[]{jsonObject, "$.married"});
        siddhiAppRuntime.shutdown();
    }
}
