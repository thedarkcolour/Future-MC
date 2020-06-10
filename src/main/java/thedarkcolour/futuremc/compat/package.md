# thedarkcolour.futuremc.compat
This package contains functions to access mod-specific functions for
inter-mod compatibility. It also contains several subpackages with
compatibility modules for certain mods. 

To make things safe, each mod has its own subpackage
with functions and classes specific to it. Access to these classes and functions
is kept safe by using functions with nullable returns. We can then use nullable
calls to mod functions so that if the mod is not loaded, the mod's functions
are never initialized.

## Actually Additions
Actually Additions compat adds pollination targets and pollination handlers 
for its flowers and crops.

## Better With Mods
Better With Mods compat adds the campfire to BWM's heat sources for use
in the Cauldron cooking pot.

## CraftTweaker
CraftTweaker compat exposes recipe systems used by Future MC to modpack makers
so they can add their own recipes.

## Dynamic Trees
Dynamic Trees compat **will eventually** allow bee nests to spawn on trees
from Dynamic Trees.

## Just Enough Items (JEI)
Just Enough Items (JEI) allows players to see recipes added by Future MC.

## Pam's Harvestcraft
Pam's Harvestcraft compat adds pollination handlers for its crops.

## Plants
Plants compat adds pollination targets and pollination handlers
for its flowers and crops.

## Quark
Quark compat controls whether shared features in Future MC are enabled.
For example, if Quark is installed, the Future MC coremod will not apply
to the BlockPistonBase class. If Quark's trapdoors are enabled, Future MC
will not override the vanilla trapdoor recipe.

## Tinkers Construct
Tinkers Construct compat allows TC axes to strip logs.
Eventually there will be molten netherite and molten ancient metal.

## Vivecraft
Vivecraft compat fixes a bytecode conflict between FutureMC and Vivecraft.