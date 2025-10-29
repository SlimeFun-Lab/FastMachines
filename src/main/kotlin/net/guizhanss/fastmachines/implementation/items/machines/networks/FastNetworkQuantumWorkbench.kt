package net.guizhanss.fastmachines.implementation.items.machines.networks

import io.github.sefiraat.networks.slimefun.network.NetworkQuantumWorkbench
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.loaders.NetworksRecipeLoader
import net.guizhanss.fastmachines.core.recipes.loaders.RecipeLoader
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class FastNetworkQuantumWorkbench(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BaseFastMachine(itemGroup, itemStack, recipeType, recipe, 1_000_000, 5_000) {

    override val craftItemMaterial: Material
        get() = Material.REINFORCED_DEEPSLATE

    override val recipeLoader: RecipeLoader
        get() = NetworksRecipeLoader(this, NetworkQuantumWorkbench::class.java)

    override fun registerPrecondition() = FastMachines.integrationService.networksEnabled
}
