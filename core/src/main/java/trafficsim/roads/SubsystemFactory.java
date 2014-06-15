package trafficsim.roads;

import static trafficsim.TrafficSimConstants.DEGUG_SUBSYSTEMS;

import java.util.ArrayList;

import lombok.val;
import paramatricCurves.ParametricCurve;
import paramatricCurves.curveDefs.C_Circular;
import paramatricCurves.curveDefs.C_Linear;
import trafficsim.TrafficSimConstants;

import com.badlogic.gdx.math.Vector2;

import editor.EditorData;
import functions.VectorUtils;

public class SubsystemFactory {

	private static final float SAME_DIR_LIMIT = 10.0f;

	public static ArrayList<Lane> createRoadSubsystem(
			ArrayList<ParametricCurve> roadDef, int shift) {

		ArrayList<Lane> result = new ArrayList<Lane>();
		result.add(createRoadSubsystemLinear(roadDef.get(0), shift));
		return result;

	}

	private static Lane createRoadSubsystemLinear(ParametricCurve roadDef,
			int shift) {

		Vector2 A = roadDef.getPoint(roadDef.getR_t().getLow());
		Vector2 B = roadDef.getPoint(roadDef.getR_t().getHigh());

		float angle = VectorUtils.getAngle(A, B);
		angle -= 90.0;
		Vector2 vShift = VectorUtils.getUnitVectorDegrees(angle);
		vShift.x *= (shift * TrafficSimConstants.LANE_WIDTH + TrafficSimConstants.LANE_WIDTH / 2);
		vShift.y *= (shift * TrafficSimConstants.LANE_WIDTH + TrafficSimConstants.LANE_WIDTH / 2);

		Vector2 Ashift = new Vector2();
		Ashift.x = A.x + vShift.x;
		Ashift.y = A.y + vShift.y;

		Vector2 Bshift = new Vector2();
		Bshift.x = B.x + vShift.x;
		Bshift.y = B.y + vShift.y;

		return new Lane(new ParametricCurve(new C_Linear(Ashift, Bshift)),
				TrafficSimConstants.LANE_WIDTH);

	}

	private static ArrayList<Lane> createRoadSubSystem(
			ArrayList<ParametricCurve> roadDef, int shift) {
		return null;
		// TODO implement method that creates multi-lane systems in general
		// issues with circular PC - no fixed shift by vector, but scaled shift
		// according to the center point of circle
	}

	public static SubSystem createCrossRoadSubSystem(Road rIN, Road rOUT,
			CrossRoad cr) {

		int cIN = rIN.getNumLanes();
		int cOUT = rOUT.getNumLanes();

		Vector2 angleIN = null;
		Vector2 angleOUT = null;

		// TODO This is causing problems because at the time (eg) the in-edge is
		// created, the out edge doesn't exist
		// yet. Maybe this and addConnection() in CrossRoad should be called for
		// all vertices after the creation of the
		// graph is finished. For now I removed the check cIN == cOUT
		if (cIN == cOUT || true) {
			ArrayList<Vector2> pIN = new ArrayList<Vector2>();
			ArrayList<Vector2> pOUT = new ArrayList<Vector2>();

			for (CrossRoadTransition crt : rIN.getRSubSystems().keySet())
				if (crt.getDestination() == cr) {
					for (int i = 0; i < rIN.getRSubSystems().get(crt)
							.getLanes().size(); i++)
						pIN.add(rIN
								.getRSubSystems()
								.get(crt)
								.getLanes()
								.get(i)
								.get(rIN.getRSubSystems().get(crt).getLanes()
										.get(i).size() - 1).getEnd());
					angleIN = rIN
							.getRSubSystems()
							.get(crt)
							.getLanes()
							.get(0)
							.get(rIN.getRSubSystems().get(crt).getLanes()
									.get(0).size() - 1).getTrajectory()
							.getEndDirection();
				}

			for (CrossRoadTransition crt : rOUT.getRSubSystems().keySet())
				if (crt.getOrigin() == cr) {
					for (int i = 0; i < rOUT.getRSubSystems().get(crt)
							.getLanes().size(); i++)
						pOUT.add(rOUT.getRSubSystems().get(crt).getLanes()
								.get(i).get(0).getStart());
					angleOUT = rOUT.getRSubSystems().get(crt).getLanes().get(0)
							.get(0).getTrajectory().getStartDirection();
				}

			for (Vector2 v : pIN)
				EditorData.debugPoints.add(v);
			for (Vector2 v : pOUT)
				EditorData.debugPoints.add(v);

			if (DEGUG_SUBSYSTEMS) {
				System.out.println(angleIN.x + " " + angleIN.y);
				System.out.println(angleOUT.x + " " + angleOUT.y);
			}

			if (Math.abs(VectorUtils.getAbsAngleDifference(angleIN, angleOUT) - 180.0) < SAME_DIR_LIMIT) {
				SubSystem result = new SubSystem();
				for (int i = 0; i < pIN.size(); i++) {
					ArrayList<Lane> ss = new ArrayList<Lane>();
					ss.add(new Lane(new ParametricCurve(new C_Linear(
							pIN.get(i), pOUT.get(i)))));
					result.addSubsystem(ss);
				}
				return result;
			} else if ((Math.abs(VectorUtils.getAbsAngleDifference(angleIN,
					angleOUT) - 90) < SAME_DIR_LIMIT)
					|| (Math.abs(VectorUtils.getAbsAngleDifference(angleIN,
							angleOUT) + 90) < SAME_DIR_LIMIT)
					|| (Math.abs(VectorUtils.getAbsAngleDifference(angleIN,
							angleOUT) - 90 - 360) < SAME_DIR_LIMIT)
					|| (Math.abs(VectorUtils.getAbsAngleDifference(angleIN,
							angleOUT) + 90 - 360) < SAME_DIR_LIMIT)) {
				if (DEGUG_SUBSYSTEMS) {
					System.out.println("yes");
				}
				boolean clockwise = false;
				angleIN.rotate(90.0f);
				if (VectorUtils.getAbsAngleDifference(angleIN, angleOUT) < SAME_DIR_LIMIT) {
					angleIN.rotate(180.0f);
					clockwise = true;
				}
				Vector2 side_rect_length = new Vector2();
				side_rect_length.x = pIN.get(0).x - pOUT.get(0).x;
				side_rect_length.y = pIN.get(0).y - pOUT.get(0).y;

				if (DEGUG_SUBSYSTEMS) {
					System.out.println(pIN.get(0));
					System.out.println(pOUT.get(0));
				}

				Vector2 vv = VectorUtils.multiplyVector(
						VectorUtils.getUnitVectorDegrees(angleIN.angle()),
						(float) Math.sqrt(side_rect_length.len2() / 2.0));
				if (DEGUG_SUBSYSTEMS) {
					System.out.println((float) Math.sqrt(side_rect_length
							.len2() / 2.0));
				}

				Vector2 circleCenter = VectorUtils.add2Vectors(vv, pIN.get(0));
				EditorData.debugPoints.add(circleCenter);

				SubSystem result = new SubSystem();
				for (int i = 0; i < pIN.size(); i++) {
					ArrayList<Lane> ss = new ArrayList<Lane>();
					Float radius = (float) VectorUtils.getLength(circleCenter,
							pIN.get(i));
					ss.add(new Lane(new ParametricCurve(new C_Circular(
							circleCenter, radius, pIN.get(i), pOUT.get(i),
							clockwise))));
					result.addSubsystem(ss);
				}
				return result;
			} else {
				if (DEGUG_SUBSYSTEMS) {
					System.out.println((Math.abs(VectorUtils
							.getAbsAngleDifference(angleIN, angleOUT) - 90)));
					System.out.println((Math.abs(VectorUtils
							.getAbsAngleDifference(angleIN, angleOUT) + 90)));
				}
			}

		} else if (cIN > cOUT) {

		} else if (cIN < cOUT) {

		}
		return null;
	}

	public static SubSystem createRoundAboutSubSystem(Road rIN, Road rOUT,
			CrossRoad cr) {

		SubSystem ss = new SubSystem();

		int cIN = rIN.getNumLanes();
		int cOUT = rOUT.getNumLanes();

		Vector2 angleIN = null;
		Vector2 angleOUT = null;

		ArrayList<Vector2> pIN = new ArrayList<Vector2>();
		ArrayList<Vector2> pOUT = new ArrayList<Vector2>();

		for (CrossRoadTransition crt : rIN.getRSubSystems().keySet())
			if (crt.getDestination() == cr) {
				for (int i = 0; i < rIN.getRSubSystems().get(crt).getLanes()
						.size(); i++)
					pIN.add(rIN
							.getRSubSystems()
							.get(crt)
							.getLanes()
							.get(i)
							.get(rIN.getRSubSystems().get(crt).getLanes()
									.get(i).size() - 1).getEnd());
				angleIN = rIN
						.getRSubSystems()
						.get(crt)
						.getLanes()
						.get(0)
						.get(rIN.getRSubSystems().get(crt).getLanes().get(0)
								.size() - 1).getTrajectory().getEndDirection();
			}

		for (CrossRoadTransition crt : rOUT.getRSubSystems().keySet())
			if (crt.getOrigin() == cr) {
				for (int i = 0; i < rOUT.getRSubSystems().get(crt).getLanes()
						.size(); i++)
					pOUT.add(rOUT.getRSubSystems().get(crt).getLanes().get(i)
							.get(0).getStart());
				angleOUT = rOUT.getRSubSystems().get(crt).getLanes().get(0)
						.get(0).getTrajectory().getStartDirection();
			}

		for (val pin : pIN)
			for (val pout : pOUT) {
				ArrayList<Lane> subsys = new ArrayList<Lane>();
				Lane lane = new Lane(new ParametricCurve(new C_Circular(
						cr.getPosition(), cr.getSize() / 2
								- TrafficSimConstants.LANE_WIDTH / 2, pin,
						pout, false)));
				subsys.add(lane);
				ss.addSubsystem(subsys);
			}

		return ss;
	}

	public static SubSystem createHighWayCrossSS_TYPE1(Road rIN, Road rOUT,
			CrossRoad cr) {

		SubSystem ss = new SubSystem();

		int cIN = rIN.getNumLanes();
		int cOUT = rOUT.getNumLanes();

		Vector2 angleIN = null;
		Vector2 angleOUT = null;

		ArrayList<Vector2> pIN = new ArrayList<Vector2>();
		ArrayList<Vector2> pOUT = new ArrayList<Vector2>();

		for (CrossRoadTransition crt : rIN.getRSubSystems().keySet())
			if (crt.getDestination() == cr) {
				for (int i = 0; i < rIN.getRSubSystems().get(crt).getLanes()
						.size(); i++)
					pIN.add(rIN
							.getRSubSystems()
							.get(crt)
							.getLanes()
							.get(i)
							.get(rIN.getRSubSystems().get(crt).getLanes()
									.get(i).size() - 1).getEnd());
				angleIN = rIN
						.getRSubSystems()
						.get(crt)
						.getLanes()
						.get(0)
						.get(rIN.getRSubSystems().get(crt).getLanes().get(0)
								.size() - 1).getTrajectory().getEndDirection();
			}

		for (CrossRoadTransition crt : rOUT.getRSubSystems().keySet())
			if (crt.getOrigin() == cr) {
				for (int i = 0; i < rOUT.getRSubSystems().get(crt).getLanes()
						.size(); i++)
					pOUT.add(rOUT.getRSubSystems().get(crt).getLanes().get(i)
							.get(0).getStart());
				angleOUT = rOUT.getRSubSystems().get(crt).getLanes().get(0)
						.get(0).getTrajectory().getStartDirection();
			}

		if (Math.abs((VectorUtils.getAngleDifference(angleIN, angleOUT) - 270.0f)) < SAME_DIR_LIMIT
				|| Math.abs((VectorUtils.getAngleDifference(angleIN, angleOUT) + 90.0f)) < SAME_DIR_LIMIT) {
			ArrayList<Lane> subsys = new ArrayList<Lane>();
			Lane lane = new Lane(new ParametricCurve(new C_Linear(pIN.get(pIN
					.size() - 1), pOUT.get(pOUT.size() - 1))));
			subsys.add(lane);
			ss.addSubsystem(subsys);

			if (DEGUG_SUBSYSTEMS)
				System.out.println(pIN.size() + " " + pOUT.size());
			EditorData.debugPoints.add(pIN.get(pIN.size() - 1));
			EditorData.debugPoints2.add(pOUT.get(pOUT.size() - 1));
		} else if (Math
				.abs((VectorUtils.getAngleDifference(angleIN, angleOUT) + 270.0f)) < SAME_DIR_LIMIT
				|| Math.abs((VectorUtils.getAngleDifference(angleIN, angleOUT) - 90.0f)) < SAME_DIR_LIMIT) {
			if (DEGUG_SUBSYSTEMS)
				System.out.println("other");
			ArrayList<Lane> subsys = new ArrayList<Lane>();

			// //
			float correctFactor = 0.65f;
			Vector2 sDir = VectorUtils.getUnitVectorDegrees(angleIN.angle());
			sDir = VectorUtils.multiplyVector(sDir, cr.getSize()
					* correctFactor);
			Vector2 sDirP = VectorUtils.add2Vectors(pIN.get(pIN.size() - 1),
					sDir);

			Vector2 eDir = VectorUtils.getUnitVectorDegrees(angleOUT.angle());
			eDir = VectorUtils.multiplyVector(eDir, cr.getSize()
					* correctFactor);
			Vector2 eDirP = VectorUtils.add2Vectors(pOUT.get(pOUT.size() - 1),
					eDir);

			int diff = cIN - cOUT;
			if (diff == 0) {
				// OK
			} else if (diff > 0) {
				// more in;
				eDirP = VectorUtils.add2Vectors(eDirP, VectorUtils
						.multiplyVector(
								VectorUtils.getUnitVectorDegrees(eDir.angle()),
								TrafficSimConstants.LANE_WIDTH * diff));
			} else if (diff < 0) {
				// more out;

				sDirP = VectorUtils.add2Vectors(sDirP, VectorUtils
						.multiplyVector(VectorUtils.getUnitVectorDegrees(sDir
								.angle() - 180.0f),
								TrafficSimConstants.LANE_WIDTH * diff));
			}
			EditorData.debugPoints.add(sDirP);
			EditorData.debugPoints2.add(eDirP);

			Lane lane1 = new Lane(new ParametricCurve(new C_Linear(pIN.get(pIN
					.size() - 1), sDirP)));
			Lane lane3 = new Lane(new ParametricCurve(new C_Linear(eDirP,
					pOUT.get(pOUT.size() - 1))));

			Vector2 side_rect_length = new Vector2();
			side_rect_length.x = sDirP.x - eDirP.x;
			side_rect_length.y = sDirP.y - eDirP.y;

			Vector2 orthogonalV = angleIN.cpy().rotate(-90.0f);

			Vector2 vv = VectorUtils.add2Vectors(sDirP, VectorUtils
					.multiplyVector(VectorUtils
							.getUnitVectorDegrees(orthogonalV.angle()),
							(float) Math.sqrt(side_rect_length.len2() / 2.0)));
			Lane lane2 = new Lane(new ParametricCurve(new C_Circular(vv,
					VectorUtils.getLength(vv, eDirP), sDirP, eDirP, true)));
			EditorData.debugPoints2.add(vv);

			// //
			subsys.add(lane1);
			subsys.add(lane2);
			subsys.add(lane3);
			ss.addSubsystem(subsys);
		} else if (Math.abs((VectorUtils.getAbsAngleDifference(angleIN,
				angleOUT)) - 180) < SAME_DIR_LIMIT) {
			ArrayList<Lane> subsys = new ArrayList<Lane>();
			for (int i = 0; i < pIN.size(); i++) {

				subsys.add(new Lane(new ParametricCurve(new C_Linear(
						pIN.get(i), pOUT.get(i)))));
				ss.addSubsystem(subsys);
			}
		}
		return ss;
	}
}
