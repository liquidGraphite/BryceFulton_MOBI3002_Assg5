/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// Started with Android Developer code sample
// Code available at https://developer.android.com/codelabs/jetpack-compose-theming#0
// ...then swapped in a DB to populate the compose list

package com.codelab.basics

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.Blue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import com.codelab.basics.DBClass

/**
 * Sample DB Compose app with Master-Details pages
 * ShowPageMaster ... shows the list of DB elements
 * ShowPageDetails ... shows the detail contents of one element
 *
 * Added Adaptive behavior...
 *  - show master and details on different screens
 *  - if landscape, show master and details side-by-side
 *
 * Use the logcat to follow the logic.
 *
 * It's waiting for real data....
 */
class MainActivity : ComponentActivity() {

    private fun getPokemonList(): List<Map<String, Any>> {
        val db = DBClass(this)
        val cursor = db.readableDatabase.query("pokemon_table", null, null, null, null, null, null)
        val pokemonList = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            pokemonList.add(
                mapOf(
                    "name" to cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    "number" to cursor.getInt(cursor.getColumnIndexOrThrow("number")),
                    "power_level" to cursor.getInt(cursor.getColumnIndexOrThrow("power_level")),
                    "description" to cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    "access_count" to cursor.getInt(cursor.getColumnIndexOrThrow("access_count"))
                )
            )
        }
        cursor.close()
        return pokemonList
    }

    private fun incrementAccessCount(pokemonName: String) {
        val db = DBClass(this)
        db.writableDatabase.execSQL(
            "UPDATE pokemon_table SET access_count = access_count + 1 WHERE name = ?", arrayOf(pokemonName)
        )
    }

    @Composable
    fun PokemonApp() {
        var selectedPokemon by remember { mutableStateOf<Map<String, Any>?>(null) }

        if (selectedPokemon == null) {
            PokemonListScreen(
                onPokemonClick = { pokemon ->
                    incrementAccessCount(pokemon["name"] as String)
                    selectedPokemon = pokemon
                }
            )
        } else {
            PokemonDetailsScreen(
                pokemon = selectedPokemon!!,
                onBack = { selectedPokemon = null }
            )
        }
    }

    @Composable
    fun PokemonListScreen(onPokemonClick: (Map<String, Any>) -> Unit) {
        val pokemonList = getPokemonList()
        val favoritePokemon = pokemonList.maxByOrNull { it["access_count"] as Int }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Favorite Pokémon: ${favoritePokemon?.get("name") ?: "None"}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(pokemonList) { pokemon ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPokemonClick(pokemon) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pokemon["name"] as String,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Access: ${pokemon["access_count"]}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun PokemonDetailsScreen(pokemon: Map<String, Any>, onBack: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = pokemon["name"] as String,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Number: ${pokemon["number"]}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Power Level: ${pokemon["power_level"]}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = pokemon["description"] as String,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Back")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PokemonApp()
            }
        }
    }
}