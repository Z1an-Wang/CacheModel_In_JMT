cd '/Users/prince_an/Desktop/IC/Final/Code/JMT_V1.1';
mvn clean;
mvn package;

echo "processing temp1..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp1.jsimg -seed 4546 -maxtime 120;

echo "processing temp2_result_analysis..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp2_result_analysis.jsimg -seed 4546 -maxtime 120;

echo "processing temp3_whatif.jsimg..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp3_whatif.jsimg -seed 4546 -maxtime 120;

echo "processing temp4_performance_indice_convergence..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp4_performance_indice_convergence.jsimg -seed 4546 -maxtime 120;

echo "processing temp5_queueing_network_probability_routing..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp5_queueing_network_probability_routing.jsimg -seed 4546 -maxtime 120;

echo "processing temp6_finite_capcity..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp6_finite_capcity.jsimg -seed 4546 -maxtime 120;

echo "processing temp7_join_fork..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp7_join_fork.jsimg -seed 4546 -maxtime 120;

echo "processing temp8_finite_capacity_region..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp8_finite_capacity_region.jsimg -seed 4546 -maxtime 120;

echo "processing temp9_priority_class..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp9_priority_class.jsimg -seed 4546 -maxtime 120;

echo "processing temp10_classswitcher..."
java -cp ./target/JMT-singlejar-1.2.1.jar jmt.commandline.Jmt sim ./examples/test/temp10_classswitcher.jsimg -seed 4546 -maxtime 120;

echo "Done!"

echo "Cleaning generated files..."
rm ./examples/test/*-result.jsim
