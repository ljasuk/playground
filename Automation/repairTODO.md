# RepairXML.java TODO

### Functions to add
- [x] tables in numeric list to grid - 'client-facing_interfaces.xml'
- [x] turn RepairXML methods from static to class methods
- [x] handle UTF-8 or appropriate encoding
- [x] too long: health_check, troubleshooting_g, UATS?, Demo?, etc.
- [ ] cut preforms that are too long (97+ char?)
- [x] charset safety check method instead of repairTM, remove readContent
- [x] turn all numeric lists to steplists
- [x] condense stepxmps that have 64+ char lines
- [ ] maybe recursive methods for lists


### Other tasks
- [x] test new [Repair](src/RepairXml.java) and
[output tester](src/OutputTester.java)
- [x] replace old [RepairXML](src/OldRepairXML.java)
- [x] test [repair](src/RepairXml.java) and check '<stpexp>' tag
- [ ] clean up whole project

### Completeness Errors
- [ ] titled-block mistery: integrationWiFi (1)
- [x] steplist in a list: troubleshootingGuide(1), healthCheck(1), VMBackup(1)
- [ ] resource-id in a resource-id: demoDescrption (1)
- [x] figures in steplists to '<p><graphics>' : VMCreationVSphere
- [ ] table in list in steplist: VMCreationVSphere(2)
- [x] xml:id starts with number: releaseNote(1)
- [ ] emph in reference list: hardwareRequirements(1) _dont bother_






