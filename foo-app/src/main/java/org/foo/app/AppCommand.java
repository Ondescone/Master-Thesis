/*
 * Copyright 2020-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.foo.app;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.impl.action.command.ManagerImpl;
import org.onosproject.cli.AbstractShellCommand;

import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.DeviceId;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.Device;
//import org.onosproject.snmp.SnmpController;
//import org.onosproject.snmp.ctl.DefaultSnmpDevice;
//import org.onosproject.openflow.controller.OpenFlowController;
//import org.onosproject.openflow.controller.OpenFlowSwitch;

import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flowobjective.DefaultForwardingObjective;
import org.onosproject.net.flowobjective.FlowObjectiveService;
import org.onosproject.net.flowobjective.ForwardingObjective;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import static org.foo.app.OsgiPropertyConstant.OsgiPropertyConstants.FLOW_PRIORITY;
import static org.foo.app.OsgiPropertyConstant.OsgiPropertyConstants.FLOW_PRIORITY_DEFAULT;

/**
 * Sample Apache Karaf CLI command
 */
@Service
@Command(scope = "onos", name = "sample",
         description = "Sample Apache Karaf CLI command")
public class AppCommand extends AbstractShellCommand {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceService deviceService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CoreService coreService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowObjectiveService flowObjectiveService;
    private final Logger log = getLogger(getClass());
    /** Configure Flow Priority for installed flow rules; default is 10. */
    private int flowPriority = FLOW_PRIORITY_DEFAULT;
    private ApplicationId appId;
    //@Reference(cardinality = ReferenceCardinality.MANDATORY)
    //protected OpenFlowController controller;


    @Override
    protected void doExecute() {
        this.deviceService = AbstractShellCommand.get(DeviceService.class);
        this.coreService = AbstractShellCommand.get(CoreService.class);
        this.flowObjectiveService = AbstractShellCommand.get(FlowObjectiveService.class);
        TrafficSelector.Builder selectorBuilder = DefaultTrafficSelector.builder();
        appId = coreService.registerApplication("org.foo.app");
        //this.controller = AbstractShellCommand.get(OpenFlowController.class);
        //DefaultSnmpDevice snmpDevice;
        //SnmpController snmpController = AbstractShellCommand.get(SnmpController.class);
        //snmpController.getDevices().forEach(d->System.out.println("Snmp id: "+d.deviceId()));
        Iterable<Device> devices = deviceService.getDevices();
        for(Device d : devices){
            print("Device id: "+d.id().toString());
        }
        print("Hello %s", "World");
        /*for (OpenFlowSwitch sw : controller.getSwitches()) {
            print("Sono qui");
        }*/
        TrafficTreatment treatment = DefaultTrafficTreatment.builder().drop()
                .build();
        ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                .withSelector(selectorBuilder.build())
                .withTreatment(treatment)
                .withPriority(flowPriority)
                .withFlag(ForwardingObjective.Flag.VERSATILE)
                .fromApp(appId)
                .add();

        DeviceId deviceId = DeviceId.deviceId("of:0000000000000001");
        flowObjectiveService.forward(deviceId,
                forwardingObjective);
    }
}
