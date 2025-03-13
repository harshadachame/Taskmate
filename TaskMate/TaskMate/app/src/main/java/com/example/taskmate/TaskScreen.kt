package com.example.taskmate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmate.ui.theme.TaskMateTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.firebase.auth.FirebaseAuth

class TaskScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val database = TaskDatabase.getInstance(LocalContext.current)
    val repository = TaskRepository(database.taskDao())
    val viewModel = TaskViewModel(repository)
//    val auth = FirebaseAuth.getInstance()

    TaskMateTheme {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }

        ) { paddingValues ->
            AnimatedNavHost(
                navController = navController,
                startDestination = "home",
//                startDestination = if (auth.currentUser != null) "home" else "login",
                modifier = Modifier.padding(paddingValues),
                enterTransition = { fadeIn(animationSpec = tween(500)) },  // Fade in transition
                exitTransition = { fadeOut(animationSpec = tween(500)) },  // Fade out transition
            ) {
                /*composable(
                    "login",
                    enterTransition = { slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(500)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(500)) }
                ) { LoginScreen(navController, auth) }

                composable(
                    "register",
                    enterTransition = { fadeIn(animationSpec = tween(500)) },  // Fade in transition
                    exitTransition = { fadeOut(animationSpec = tween(500)) },  // Fade out transition
                ){RegisterScreen(navController,auth)}*/

                composable(
                    "home",
                    enterTransition = { slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(500)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(500)) }
                ) { TaskListScreen(navController, viewModel) }

                composable(
                    "profile",
                    enterTransition = { slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(500)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(500)) }
                ) { ProfileScreen() }

                composable(
                    "other",
                    enterTransition = { fadeIn(animationSpec = tween(500)) },
                    exitTransition = { fadeOut(animationSpec = tween(500)) }
                ) { OtherScreen() }

                composable(
                    "addTask",
                    enterTransition = { slideInVertically(initialOffsetY = { 300 }, animationSpec = tween(500)) },
                    exitTransition = { slideOutVertically(targetOffsetY = { -300 }, animationSpec = tween(500)) }
                ) { AddTaskScreen(navController, viewModel) }

                composable(
                    "editTask/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
                    enterTransition = { slideInVertically(initialOffsetY = { 300 }, animationSpec = tween(500)) },
                    exitTransition = { slideOutVertically(targetOffsetY = { -300 }, animationSpec = tween(500)) }
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
                    EditTaskScreen(navController, viewModel, taskId)
                }
            }
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Login failed"
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Don't have an account? Register")
                }
            }
        }
    }
}*/

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Register") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }

                        isLoading = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    navController.navigate("home") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Registration failed"
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Already have an account? Login")
                }
            }
        }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, viewModel: TaskViewModel) {
    val tasks by viewModel.allTasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("TaskMate", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.blue), // 👈 Sets Background Color of TopAppBar
                    titleContentColor = Color.White // 👈 Ensures Text Color is White
                ),
               // modifier = Modifier.height(48.dp) // 👈 Reduce height (default is ~56.dp)
            )
        },
        //bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addTask") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onTaskClick = { navController.navigate("editTask/${task.id}") },
                    onTaskCompleted = {
                        val updatedTask = task.copy(isCompleted = !task.isCompleted)
                        viewModel.updateTask(updatedTask)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("profile", Icons.Default.Person, "Profile"),
        BottomNavItem("other", Icons.Default.Settings, "Other")
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route

            // 🔥 Scale animation for icon
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.5f else 1f, // Enlarge selected icon
                animationSpec = tween(durationMillis = 300), label = ""
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.scale(scale) // ✅ Apply scale animation
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Blue,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Blue,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() { // ✅ Pass navController
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile Screen", fontSize = 24.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen() { // ✅ Pass navController
    Scaffold(
        topBar = { TopAppBar(title = { Text("Other") }) },

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Other Screen", fontSize = 24.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController, viewModel: TaskViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Low") }
    var category by remember { mutableStateOf("General") }
    val dueDate by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Task Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Task Description") }, modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Low", "Medium", "High").forEach { p ->
                    RadioButton(selected = priority == p, onClick = { priority = p })
                    Text(text = p, modifier = Modifier.align(Alignment.CenterVertically))
                }
            }

            DropdownMenuComponent(selectedCategory = category, onCategorySelected = { category = it })

            Button(
                onClick = {
                    val newTask = Task(title = title, description = description, priority = priority, category = category, dueDate = dueDate)
                    viewModel.addTask(newTask)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(navController: NavController, viewModel: TaskViewModel, taskId: Int) {
    val task = viewModel.getTaskById(taskId).collectAsState(initial = null).value
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var priority by remember { mutableStateOf(task?.priority ?: "Low") }
    var category by remember { mutableStateOf(task?.category ?: "General") }
    var dueDate by remember { mutableLongStateOf(task?.dueDate ?: System.currentTimeMillis()) }

    // Update UI fields when task is fetched
    LaunchedEffect(task) {
        task?.let {
            title = it.title
            description = it.description
            priority = it.priority
            category = it.category
            dueDate = it.dueDate!!
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.deleteTask(task!!)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Task Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Task Description") }, modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Low", "Medium", "High").forEach { p ->
                    RadioButton(selected = priority == p, onClick = { priority = p })
                    Text(text = p, modifier = Modifier.align(Alignment.CenterVertically))
                }
            }

            DropdownMenuComponent(selectedCategory = category, onCategorySelected = { category = it })

            Button(
                onClick = {
                    val updatedTask = Task(id = task!!.id, title = title, description = description, priority = priority, category = category, dueDate = dueDate)
                    viewModel.updateTask(updatedTask)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Work", "Personal", "Shopping", "Health", "Other")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedCategory)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)  // Update selected category
                        expanded = false  // Close menu
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskClick: () -> Unit, onTaskCompleted: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskCompleted() }
            )

            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)) {
                Text(task.title, style = MaterialTheme.typography.bodyLarge)
                Text(task.category, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Text(
                text = task.priority,
                color = when (task.priority) {
                    "High" -> Color.Red
                    "Medium" -> Color.Yellow
                    else -> Color.Green
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    val sampleTasks = listOf(
        Task(id = 1, title = "Buy Groceries", description = "Milk, Bread, Eggs", priority = "High", category = "Shopping", isCompleted = false),
        Task(id = 2, title = "Workout", description = "Morning Exercise", priority = "Medium", category = "Health", isCompleted = true)
    )

    TaskMateTheme {
        Scaffold {
            LazyColumn {
                items(sampleTasks) { task ->
                    TaskItem(task = task, onTaskClick = {}, onTaskCompleted = {})
                }
            }
        }
    }
}

