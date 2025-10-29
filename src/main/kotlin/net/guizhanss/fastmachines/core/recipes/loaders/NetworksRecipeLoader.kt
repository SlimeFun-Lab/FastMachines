package net.guizhanss.fastmachines.core.recipes.loaders

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun
import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.choices.ExactChoice
import net.guizhanss.fastmachines.core.recipes.raw.RawRecipe
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import net.guizhanss.fastmachines.utils.items.countItems
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

class NetworksRecipeLoader(
    machine: BaseFastMachine,
    private val clazz: Class<out SlimefunItem>,
    enableRandomRecipes: Boolean = false,
) : RecipeLoader(machine, enableRandomRecipes) {

    override fun beforeLoad() {
        val type: RecipeType = try {
            val f = clazz.getDeclaredField("TYPE")
            f.isAccessible = true
            f.get(null) as RecipeType
        } catch (e: Exception) {
            FastMachines.log(Level.SEVERE, e, "NetworksRecipeLoader: failed to read TYPE from ${clazz.name}")
            return
        }

        val registryItems = try {
            Slimefun.getRegistry().enabledSlimefunItems
        } catch (e: Exception) {
            FastMachines.log(Level.SEVERE, e, "NetworksRecipeLoader: failed to access Slimefun registry")
            return
        }

        var found = 0
        for (sfItem in registryItems) {
            try {
                if (sfItem.recipeType != type) continue

                val grid: Array<ItemStack?> = sfItem.recipe ?: emptyArray()
                val inputs = grid
                    .filterNotNull()
                    .toList()
                    .countItems()
                    .map { (item, amount) -> ExactChoice(item, amount) }

                val output = sfItem.recipeOutput ?: continue

                rawRecipes.add(
                    RawRecipe(
                        inputs,
                        listOf(output)
                    )
                )
                found++
            } catch (e: Exception) {
                FastMachines.log(Level.WARNING, e, "NetworksRecipeLoader: error while reading recipe for ${sfItem.id}")
            }
        }

        FastMachines.debug("NetworksRecipeLoader: loaded $found recipes from RecipeType=${type.key.key} (${clazz.simpleName})")
    }
}
