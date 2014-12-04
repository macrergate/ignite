/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.tests.p2p;

import org.apache.ignite.*;
import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.util.typedef.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Test task for {@code GridP2PContinuousDeploymentSelfTest}.
 */
public class GridP2PContinuousDeploymentTask1 extends GridComputeTaskSplitAdapter<Object, Object> {
    /** {@inheritDoc} */
    @Override protected Collection<? extends GridComputeJob> split(int gridSize, Object arg) throws GridException {
        return Collections.singleton(new GridComputeJobAdapter() {
            @GridInstanceResource
            private Ignite ignite;

            @Override public Object execute() throws GridException {
                X.println(">>> Executing GridP2PContinuousDeploymentTask1 job.");

                ignite.cache(null).putx("key", new GridTestUserResource());

                return null;
            }
        });
    }

    /** {@inheritDoc} */
    @Nullable @Override public Object reduce(List<GridComputeJobResult> results) throws GridException {
        return null;
    }
}
