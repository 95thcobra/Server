package com.elvarg.world.model;

import com.elvarg.world.entity.Entity;
import com.elvarg.world.entity.combat.CombatFactory;
import com.elvarg.world.entity.combat.bountyhunter.PvpHandler;
import com.elvarg.world.entity.impl.Character;
import com.elvarg.world.entity.impl.npc.NPC;
import com.elvarg.world.entity.impl.player.Player;

public class Locations {

	public static void login(Player player) {
		player.setLocation(Location.getLocation(player));
		player.getLocation().login(player);
		player.getLocation().enter(player);
	}

	public static void logout(Player player) {
		player.getLocation().logout(player);
		player.getLocation().leave(player);
	}

	public static int PLAYERS_IN_WILD;

	public enum Location {
		WILDERNESS(new int[]{2940, 3392, 2986, 3012, 3653, 3720, 3650, 3653, 3150, 3199, 2994, 3041}, new int[]{3525, 3968, 10338, 10366, 3441, 3538, 3457, 3472, 3796, 3869, 3733, 3790}, false, true, true, true, true, true, false) {
			@Override
			public void process(Player player) {
				int y = player.getPosition().getY();
				player.setWildernessLevel(((((y > 6400 ? y - 6400 : y) - 3520) / 8)+1));
				player.getPacketSender().sendString(23321, "Level: "+player.getWildernessLevel());
			}

			@Override
			public void leave(Player player) {
				PvpHandler.onLeave(player);
			}

			@Override
			public void login(Player player) {
				enter(player);
			}

			@Override
			public void enter(Player player) {
				PvpHandler.onEnter(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				if(player.getWildernessLevel() > 20 && player.getRights() != PlayerRights.DEVELOPER) {
					player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
					player.getPacketSender().sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
					return false;
				}
				return true;
			}

			@Override
			public boolean canAttack(Player attacker, Player target) {
				int combatDifference = CombatFactory.combatLevelDifference(attacker.getSkillManager().getCombatLevel(), target.getSkillManager().getCombatLevel());
				if (combatDifference > attacker.getWildernessLevel() + 5 || combatDifference > target.getWildernessLevel() + 5) {
					attacker.getPacketSender().sendMessage("Your combat level difference is too great to attack that player here.").sendMessage("Move deeper into the wilderness first.");
					attacker.getMovementQueue().reset();
					return false;
				}
				if (!PvpHandler.PLAYERS_IN_PVP.contains(target)) {
					attacker.getPacketSender().sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
					attacker.getMovementQueue().reset();
					return false;
				}
				return true;
			}
		},
		GRAND_EXCHANGE_PVP(new int[]{3137, 3186, 3139, 3186, 3179, 3188, 3182, 3200, 3182, 3139, 3139, 3147, 3137, 3188}, new int[]{3468, 3472, 3473, 3473, 3478, 3486, 3487, 3516, 3494, 3507, 3473, 3516, 3507, 3516}, false, true, true, true, true, true, false) {
			@Override
			public void process(Player player) {
				player.getPacketSender().sendWalkableInterface(21200);
			}

			@Override
			public void leave(Player player) {
				PvpHandler.onLeave(player);
			}

			@Override
			public void login(Player player) {
				enter(player);
			}

			@Override
			public void enter(Player player) {
				PvpHandler.onEnter(player);
				player.getPacketSender().sendMessage("@red@Welcome to Safe Pking. You will NOT lose your items here!");
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public boolean canAttack(Player attacker, Player target) {
				if (!PvpHandler.PLAYERS_IN_PVP.contains(target)) {
					attacker.getPacketSender().sendMessage("That player cannot be attacked, because they are not in the Pvp Area..");
					attacker.getMovementQueue().reset();
					return false;
				}
				return true;
			}
		},
		VARROCK(new int[]{3101, 3268, 3187, 3234, 3115, 3179, 3165, 3250, 3191, 3200}, new int[]{3448, 3468, 3458, 3506, 3392, 3447, 3390, 3432, 3432, 3460}, false, true, true, true, true, true, false) {
			@Override
			public void process(Player player) {
				player.getPacketSender().sendWalkableInterface(21200);
			}

			@Override
			public void leave(Player player) {
				PvpHandler.onLeave(player);
			}

			@Override
			public void login(Player player) {
				enter(player);
			}

			@Override
			public void enter(Player player) {
				PvpHandler.onEnter(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public boolean canAttack(Player attacker, Player target) {
				if (!PvpHandler.PLAYERS_IN_PVP.contains(target)) {
					attacker.getPacketSender().sendMessage("That player cannot be attacked, because they are not in the Pvp Area..");
					attacker.getMovementQueue().reset();
					return false;
				}
				return true;
			}
		},
		DEFAULT(null, null, false, true, true, true, true, true, true) {
			@Override
			public void process(Player p) {
				p.getPacketSender().sendWalkableInterface(21300);
				p.getPacketSender().sendInteractionOption("null", 2, true);
				p.getPacketSender().sendInteractionOption("null", 1, false);
			}
			@Override
			public boolean canAttack(Player attacker, Player target) {
				//TODO: Remove this.
				/*if(attacker.getDueling().getState() == DuelState.IN_DUEL) {
					if(target.getDueling().getState() == DuelState.IN_DUEL) {
						return true;
					}
				} else if(attacker.getDueling().getState() == DuelState.STARTING_DUEL
						|| target.getDueling().getState() == DuelState.STARTING_DUEL) {
					DialogueManager.sendStatement(attacker, "The duel hasn't started yet!");
					return false;
				}*/
				return false;
			}
			@Override
			public boolean canTeleport(Player player) {
				/*if(player.getDueling().inDuel()) {
					DialogueManager.sendStatement(player, "You cannot teleport in a duel!");
					return false;
				}*/
				return true;
			}
		};

		Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed, boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed, boolean spawningallowed) {
			this.x = x;
			this.y = y;
			this.multi = multi;
			this.summonAllowed = summonAllowed;
			this.followingAllowed = followingAllowed;
			this.cannonAllowed = cannonAllowed;
			this.firemakingAllowed = firemakingAllowed;
			this.aidingAllowed = aidingAllowed;
			this.spawningallowed = spawningallowed;
		}

		private int[] x, y;
		private boolean multi;
		private boolean summonAllowed;
		private boolean followingAllowed;
		private boolean cannonAllowed;
		private boolean firemakingAllowed;
		private boolean aidingAllowed;
		private boolean spawningallowed;

		public int[] getX() {
			return x;
		}

		public int[] getY() {
			return y;
		}

		public static boolean inMulti(Character gc) {
			if(gc.getLocation() == WILDERNESS) {
				int x = gc.getPosition().getX(), y = gc.getPosition().getY();
				if(x >= 3155 && y >= 3798 || x >= 3020 && x <= 3055 && y >= 3684 && y <= 3711 || x >= 3150 && x <= 3195 && y >= 2958 && y <= 3003 || x >= 3645 && x <= 3715 && y >= 3454 && y <= 3550 || x>= 3150 && x <= 3199 && y >= 3796 && y <= 3869 || x >= 2994 && x <= 3041 && y >= 3733 && y <= 3790)
					return true;
			}
			return gc.getLocation().multi;
		}

		public boolean isSummoningAllowed() {
			return summonAllowed;
		}

		public boolean isSpawningAllowed() {
			return spawningallowed;
		}

		public boolean isFollowingAllowed() {
			return followingAllowed;
		}

		public boolean isCannonAllowed() {
			return cannonAllowed;
		}

		public boolean isFiremakingAllowed() {
			return firemakingAllowed;
		}

		public boolean isAidingAllowed() {
			return aidingAllowed;
		}

		public static Location getLocation(Entity gc) {
			for(Location location : Location.values()) {
				if(location != Location.DEFAULT)
					if(inLocation(gc, location))
						return location;
			}
			return Location.DEFAULT;
		}

		public static boolean inLocation(Entity gc, Location location) {
			if(location == Location.DEFAULT) {
				if(getLocation(gc) == Location.DEFAULT)
					return true;
				else
					return false;
			}
			/*if(gc instanceof Player) {
				Player p = (Player)gc;
				if(location == Location.TRAWLER_GAME) {
					String state = FishingTrawler.getState(p);
					return (state != null && state.equals("PLAYING"));
				} else if(location == FIGHT_PITS_WAIT_ROOM || location == FIGHT_PITS) {
					String state = FightPit.getState(p), needed = (location == FIGHT_PITS_WAIT_ROOM) ? "WAITING" : "PLAYING";
					return (state != null && state.equals(needed));
				} else if(location == Location.SOULWARS) {
					return (SoulWars.redTeam.contains(p) || SoulWars.blueTeam.contains(p) && SoulWars.gameRunning);
				} else if(location == Location.SOULWARS_WAIT) {
					return SoulWars.isWithin(SoulWars.BLUE_LOBBY, p) || SoulWars.isWithin(SoulWars.RED_LOBBY, p);
				}
			}
			 */
			return inLocation(gc.getPosition().getX(), gc.getPosition().getY(), location);
		}

		public static boolean inLocation(int absX, int absY, Location location) {
			int checks = location.getX().length - 1;
			for(int i = 0; i <= checks; i+=2) {
				if(absX >= location.getX()[i] && absX <= location.getX()[i + 1]) {
					if(absY >= location.getY()[i] && absY <= location.getY()[i + 1]) {
						return true;
					}
				}
			}
			return false;
		}

		public void process(Player player) {

		}

		public boolean canTeleport(Player player) {
			return true;
		}

		public void login(Player player) {

		}

		public void enter(Player player) {

		}

		public void leave(Player player) {

		}

        public void canSpawn(Player player) {

        }

		public void logout(Player player) {

		}

		public void onDeath(Player player) {

		}

		public boolean handleKilledNPC(Player killer, NPC npc) {
			return false;
		}

		public boolean canAttack(Player attacker, Player target) {
			return false;
		}
	}

	public static void onTick(Character gc) {
		Location newLocation = Location.getLocation(gc);
		if(gc.getLocation() == newLocation) {
			if(gc.isPlayer()) {
				Player player = (Player) gc;
				gc.getLocation().process(player);
				if(Location.inMulti(player)) {
					if(player.getMultiIcon() != 1)
						player.getPacketSender().sendMultiIcon(1);
				} else if(player.getMultiIcon() == 1)
					player.getPacketSender().sendMultiIcon(0);
			}
		} else {
			Location prev = gc.getLocation();
			if(gc.isPlayer()) {
				Player player = (Player) gc;
				if(player.getMultiIcon() > 0)
					player.getPacketSender().sendMultiIcon(0);
				if(player.getWalkableInterfaceId() > 0)
					player.getPacketSender().sendWalkableInterface(-1);
				if(player.getPlayerInteractingOption() != PlayerInteractingOption.NONE) {
					player.getPacketSender().sendInteractionOption("null", 2, true);
				}
			}
			gc.setLocation(newLocation);
			if(gc.isPlayer()) {
				prev.leave(((Player)gc));
				gc.getLocation().enter(((Player)gc));
			}
		}
	}

}
