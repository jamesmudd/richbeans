package org.eclipse.dawnsci.analysis.examples.pipelines;

import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;
import org.eclipse.dawnsci.analysis.api.processing.ExecutionEvent;
import org.eclipse.dawnsci.analysis.api.processing.IExecutionVisitor;
import org.eclipse.dawnsci.analysis.api.processing.IOperation;
import org.eclipse.dawnsci.analysis.api.processing.IOperationService;
import org.eclipse.dawnsci.analysis.api.processing.OperationData;
import org.eclipse.dawnsci.analysis.api.processing.model.IOperationModel;
import org.eclipse.dawnsci.analysis.dataset.impl.Random;
import org.eclipse.dawnsci.analysis.dataset.processing.RichDataset;
import org.eclipse.dawnsci.analysis.dataset.roi.SectorROI;
import org.eclipse.dawnsci.analysis.examples.Activator;
import org.junit.Before;
import org.junit.Test;


/**
 * Examples for running pipelines
 * 
 * @author Matthew Gerring
 *
 */
public class PipelineExamples {

	private IOperationService service;
	
	@Before
	public void assignService() {
		service = (IOperationService)Activator.getService(IOperationService.class);
	}
	
	/**
	 * Operations are contributed by extension point
	 * @throws Exception 
	 */
	@Test
	public void listOperations() throws Exception {
		
		System.out.println("-------------------------------");
		System.out.println("List of available operations:");
		for(String id : service.getRegisteredOperations()) {
			final IOperation op = service.create(id);
			System.out.println("'"+id+"' \t\t is called: "+op.getName());
		}
		System.out.println("-------------------------------");
	}
	
	/**
	 * In this example we do an azimuthal integration over 24 images in a LazyDataset
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void simplePipelineExample() throws Exception {
				
		// Create a Rich dataset without the extra information and a random array.
		final RichDataset   rand = new RichDataset(Random.lazyRand(24, 1000, 1000), null); // Simplest possible 
		rand.setSlicing("all"); // All 24 images in first dimension.
		
		// We access the model in a generic way because this is example code unable to see
		// the internals of pipelines.
		final IOperation<IOperationModel, OperationData> azi = (IOperation<IOperationModel, OperationData>) service.findFirst("uk.ac.diamond.scisoft.analysis.processing.operations.azimuthalIntegration");
		IOperationModel model = service.getModelClass(azi.getId()).newInstance();
		model.set("region", new SectorROI(500.0, 500.0, 20.0, 300.0,  Math.toRadians(90.0), Math.toRadians(180.0)));
		azi.setModel(model);
		
		// We do an azimuthal integration on each of the 24 images, we do not do anything with the integration
		// The series is executed in one thread, in order.
		service.executeSeries(rand, azi);
		
		// We execute using fork/join in Java7
		service.executeParallelSeries(rand, azi);

	}
		
	@Test
	public void simplePipelineExampleUsingMonitor() throws Exception {
		
		// Create a Rich dataset without the extra information and a random array.
		final RichDataset   rand = new RichDataset(Random.lazyRand(24, 1000, 1000), null); // Simplest possible 
		rand.setSlicing("all"); // All 24 images in first dimension.
		
		// We access the model in a generic way because this is example code unable to see
		// the internals of pipelines.
		final IOperation<IOperationModel, OperationData> azi = (IOperation<IOperationModel, OperationData>) service.findFirst("uk.ac.diamond.scisoft.analysis.processing.operations.azimuthalIntegration");
		IOperationModel model = service.getModelClass(azi.getId()).newInstance();
		model.set("region", new SectorROI(500.0, 500.0, 20.0, 300.0,  Math.toRadians(90.0), Math.toRadians(180.0)));
		azi.setModel(model);
		
		// We do not use IProgressMonitor because it would introduce an eclipse
		// dependency on the core mathematics.
		final IMonitor monitor = new IMonitor.Stub() {
			int total = 0;
			@Override
			public void worked(int amount) {
				total = total+amount;
				System.out.println("This amount worked "+total);
			}
		};
		
		// We do an azimuthal integration on each of the 24 images, we do not do anything with the integration
		// The series is executed in one thread, in order.
		service.executeSeries(rand, monitor, null, azi);
		
		// We execute using fork/join in Java7
		service.executeParallelSeries(rand, monitor, null, azi);

	}

	
	@Test
	public void simplePipelineExampleUsingVisitor() throws Exception {
		
		// Create a Rich dataset without the extra information and a random array.
		final RichDataset   rand = new RichDataset(Random.lazyRand(24, 1000, 1000), null); // Simplest possible 
		rand.setSlicing("all"); // All 24 images in first dimension.
		
		// We access the model in a generic way because this is example code unable to see
		// the internals of pipelines.
		final IOperation<IOperationModel, OperationData> azi = (IOperation<IOperationModel, OperationData>) service.findFirst("uk.ac.diamond.scisoft.analysis.processing.operations.azimuthalIntegration");
		IOperationModel model = service.getModelClass(azi.getId()).newInstance();
		model.set("region", new SectorROI(500.0, 500.0, 20.0, 300.0,  Math.toRadians(90.0), Math.toRadians(180.0)));
		azi.setModel(model);
		
		// We do not use IProgressMonitor because it would introduce an eclipse
		// dependency on the core mathematics.
		final IExecutionVisitor visitor = new IExecutionVisitor.Stub() {
			@Override
			public void notify(ExecutionEvent evt) {
				System.out.println("Did operation "+evt.getIntermediateData().getName());
			}
		};
		
		// We do an azimuthal integration on each of the 24 images, we do not do anything with the integration
		// The series is executed in one thread, in order.
		service.executeSeries(rand, null, visitor, azi);
	
	}

}
